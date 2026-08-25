package com.vdggrtf.playlog.data.repositoryimpl

import com.vdggrtf.playlog.data.network.api.GeminiKtorClient
import com.vdggrtf.playlog.data.network.dto.rawg.AchievementDto
import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.domain.model.AiGameRecommendation
import com.vdggrtf.playlog.domain.model.AiGameResponse
import com.vdggrtf.playlog.domain.repository.AiRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class AiRepositoryImpl (
    private val supabase: SupabaseClient,
    private val geminiClient: GeminiKtorClient
) : AiRepository {

    /*// gemini model
    private val generativeModel = GenerativeModel(
        modelName = "gemini-3.1-flash-lite-preview",
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = generationConfig {
            temperature = 0.1f
        },
        requestOptions = RequestOptions(
            apiVersion = "v1beta",
        )
    )
    private val gson = Gson()
    private val difficultyCache = mutableMapOf<String, AchievementDifficulty>()*/

    private val jsonParser = Json { ignoreUnknownKeys = true }

    override suspend fun getGameRecommendation(userRequest: String): List<AiGameRecommendation> {
        return try {
            val prompt = """
                You are a professional video game journalist and expert.
                The user asks for game recommendations: "$userRequest".
            
                Your task to find 3-5 games that perfectly match this request.
            
                CRITICAL RULES:
                1. You MUST answer STRICTLY in valid JSON format.
                2. Do NOT use markdown formatting. Do NOT wrap the answer in ```json. Just raw JSON.
                3. The "gameName" field MUST be the original official English title of the game (for API searching).
                4. The "reason" field MUST be written in ENGLISH language. Provide a short, engaging explanation of why this game fits the user's request.
            
                JSON Structure:
                {
                  "recommendations":[
                    {
                      "gameName": "The Witcher 3: Wild Hunt",
                      "reason": "This game perfectly fits your request because it has a deep storyline, a massive open world, and an excellent combat system."
                    }
                  ]
                }
            """.trimIndent()

            // 1. Делаем запрос (Клиент сам подставит модель 3.1-flash-lite и температуру 0.1)
            val rawText = geminiClient.generateText(prompt)
            if (rawText == "ERROR") return emptyList()

            val cleanJson = rawText.replace("```json", "", ignoreCase = true)
                .replace("```", "")
                .trim()

            if (!cleanJson.startsWith("{")) return emptyList()

            // Парсим DTO из сети
            val parsedResponse = jsonParser.decodeFromString<AiGameResponse>(cleanJson)

            // 4. Мапим DTO в доменные модели
            parsedResponse.recommendations.map { dto ->
                AiGameRecommendation(
                    gameName = dto.gameName,
                    reason = dto.reason
                )
            }

        } catch (e: Exception) {
            println("GeminiHelper Ошибка: ${e.message}")
            emptyList()
        }
    }



    /*override suspend fun getGameRecommendation(userRequest: String): List<AiGameRecommendation> {
        return withContext(Dispatchers.IO) {
            try {
                val prompt = """
    You are a professional video game journalist and expert.
    The user asks for game recommendations: "$userRequest".

    Your task to find 3-5 games that perfectly match this request.

    CRITICAL RULES:
    1. You MUST answer STRICTLY in valid JSON format.
    2. Do NOT use markdown formatting. Do NOT wrap the answer in ```json. Just raw JSON.
    3. The "gameName" field MUST be the original official English title of the game (for API searching).
    4. The "reason" field MUST be written in ENGLISH language. Provide a short, engaging explanation of why this game fits the user's request.

    JSON Structure:
    {
      "recommendations":[
        {
          "gameName": "The Witcher 3: Wild Hunt",
          "reason": "This game perfectly fits your request because it has a deep storyline, a massive open world, and an excellent combat system."
        }
      ]
    }
""".trimIndent()

                val response = generativeModel.generateContent(prompt)
                val rawText = response.text
                    ?: return@withContext emptyList()

                val cleanJson = rawText.replace("```json", "").replace("```", "").trim()

                if (!cleanJson.startsWith("{")) return@withContext emptyList()

                val result = gson.fromJson(cleanJson, AiGameResponse::class.java)

                result?.recommendations ?: emptyList()

            } catch (e: Exception) {
                println("GeminiHelper Ошибка: ${e.message}")
                emptyList()
            }
        }
    }*/

    override suspend fun evaluateGameDifficulty(gameName: String, achievements: List<AchievementDto>): AchievementDifficulty {
        return try {

            val achivsText = if (achievements.isEmpty()) {
                "No achievements available."
            } else {
                achievements.joinToString("\n") { "- ${it.name}: ${it.description}" }
            }

            val prompt = """
                You are a hardcore gamer expert. 
                Evaluate the difficulty of achieving 100% completion (Platinum trophy / All achievements) in the game "$gameName".
                
                Here is the list of achievements for context:
                $achivsText
                
                Based on this, reply STRICTLY with ONLY ONE of the following words: EASY, MEDIUM, HARD, DEMON, IMPOSSIBLE.
                No other text.
            """.trimIndent()

            // Дергаем наш Ktor клиент (текстовый)
            val response = geminiClient.generateText(prompt).trim().uppercase()

            // Парсим ответ ИИ в наш Enum
            when {
                response.contains("EASY") -> AchievementDifficulty.EASY
                response.contains("MEDIUM") -> AchievementDifficulty.MEDIUM
                response.contains("HARD") -> AchievementDifficulty.HARD
                response.contains("DEMON") -> AchievementDifficulty.DEMON
                response.contains("IMPOSSIBLE") -> AchievementDifficulty.IMPOSSIBLE
                else -> AchievementDifficulty.NONE
            }
        } catch (e: Exception) {
            println("AI_ERROR: Ошибка оценки сложности для $gameName -> ${e.message}")
            AchievementDifficulty.NONE
        }
    }

    /*override suspend fun evaluateGameDifficulty(gameName: String): AchievementDifficulty {
        return withContext(Dispatchers.IO) {

            if (difficultyCache.containsKey(gameName)) {
                println("GeminiHelper [КЭШ]: Сложность для $gameName уже известна!")
                return@withContext difficultyCache[gameName]!!
            }


            try {
                val prompt = """
    You are a hardcore achievement hunter and 100% completion expert.
    Evaluate the REAL difficulty of getting 100% achievements/trophies in the game "$gameName".
    
    CRITICAL RULES (Check in this exact order):
    1. IMPOSSIBLE: If the game has ANY unobtainable achievements (servers closed, removed maps like CS:GO, heavily glitched trophies) making honest 100% impossible today, you MUST return IMPOSSIBLE.
    2. DEMON: If the game is a "Souls-like" (e.g., Dark Souls, Elden Ring, Bloodborne, Sekiro), requires flawless no-hit/no-death runs, or demands insane RNG grinding (like farming items with <1% drop rate), you MUST return DEMON.
    
    If 100% is obtainable and it's not a DEMON, choose ONE word based on the hardest achievement:
    EASY - press X to win, visual novels, Telltale games, under 10 hours for platinum.
    MEDIUM - just beat the story and collect standard items.
    HARD - heavy grind (100+ hours), hard mode playthroughs, tough multiplayer, but doable with patience.
    
    Return ONLY ONE WORD from the list: NONE, EASY, MEDIUM, HARD, DEMON, or IMPOSSIBLE. No punctuation.
""".trimIndent()

                val response = generativeModel.generateContent(prompt)
                val answer = response.text?.trim()?.uppercase() ?: ""

                println("GeminiHelper [AI Difficulty]: Игра $gameName -> Ответ ИИ: $answer")

                val difficulty = when {
                    answer.contains("EASY") -> AchievementDifficulty.EASY
                    answer.contains("MEDIUM") -> AchievementDifficulty.MEDIUM
                    answer.contains("HARD") -> AchievementDifficulty.HARD
                    answer.contains("DEMON") -> AchievementDifficulty.DEMON
                    answer.contains("IMPOSSIBLE") -> AchievementDifficulty.IMPOSSIBLE
                    else -> AchievementDifficulty.NONE
                }

                if (difficulty != AchievementDifficulty.NONE) {
                    difficultyCache[gameName] = difficulty
                }

                difficulty
            } catch (e: Exception) {
                println("GeminiHelper [AI Difficulty Error]: ${e.message}")
                AchievementDifficulty.NONE
            }
        }
    }*/

    override suspend fun verifyScreenshot(imageBytes: ByteArray, gameName: String): Boolean {
        return try {
            val prompt = """
                You are a strict Anti-Cheat AI for a gaming tracker app.
                Analyze this gameplay screenshot for the game "$gameName". 
                Does this image prove 100% completion of the game? 
                (Look for: Platinum trophy pop-ups, 100% save file stats, or end-game credits).
                Reply STRICTLY with ONLY one word: TRUE or FALSE.
            """.trimIndent()

            // 💥 Дергаем Ktor клиент, который сам переведет байты в Base64 и отправит Гуглу!
            val responseText = geminiClient.analyzeImage(prompt, imageBytes)

            // Если ИИ ответил TRUE - возвращаем true!
            responseText.trim().contains("TRUE", ignoreCase = true)

        } catch (e: Exception) {
            println("AI_ERROR: Сбой проверки скриншота -> ${e.message}")
            false
        }
    }

    /*override suspend fun verifyScreenshot(
        imageBytes: ByteArray,
        gameName: String,
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                println("GeminiHelper [AI Scanner]: Начинаем анализ скриншота для игры $gameName")

                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                val prompt = """
                    You are a strict anti-cheat moderator for a gaming tracker app.
                    Look at the provided screenshot.
                    
                    Task: Determine if this screenshot proves that the user has achieved 100% completion for the game "$gameName".
                    The screenshot can be in ANY language (especially Russian or English) and from ANY platform (Steam, PS, Xbox, Mobile, In-game UI).
                    
                    Check for context clues:
                    - "100%" or "13 / 13", "50 / 50" (current equals max).
                    - "Выполнено достижений", "Получены все достижения", "Platinum".
                    - A fully filled progress bar (like the green bar in the image).
                    
                    Return EXACTLY ONE WORD:
                    TRUE - if it clearly proves 100% completion.
                    FALSE - if it does not prove 100% completion.
                """.trimIndent()

                val response = generativeModel.generateContent(content {
                    image(bitmap)
                    text(prompt)
                })

                val answer = response.text?.trim()?.uppercase() ?: "FALSE"
                println("GeminiHelper [AI Scanner]: Вердикт ИИ -> $answer")

                answer.contains("TRUE")
            } catch (e: Exception) {
                println("GeminiHelper [AI Scanner Error]: ${e.message}")
                false
            }
        }
    }*/

    override suspend fun scanLibraryForGames(imageBytes: ByteArray): List<String> {
        return try {
            println("GeminiHelper [AI Scanner]: Анализ скриншота библиотеки...")
            val prompt = """
                    You are an OCR and gaming expert. Extract the names of all video games visible in this screenshot of a game library (Steam, PlayStation, Xbox, Mobile, etc.).
                    Ignore UI elements, hours played, achievement text, or friend lists. Extract JUST the game titles.
                    
                    Return EXACTLY AND ONLY a valid JSON array of strings. No markdown, no ```json tags.
                    Example of output:["The Witcher 3: Wild Hunt", "Cyberpunk 2077", "Brawl Stars"]
                """.trimIndent()

            val responseText = geminiClient.analyzeImage(prompt = prompt, imageBytes = imageBytes)

            if (responseText == "ERROR") return emptyList()

            // 2. Чистим текст от Markdown (на случай, если ИИ ослушается)
            val cleanJson = responseText.replace("```json", "", ignoreCase = true)
                .replace("```", "")
                .trim()

            if (!cleanJson.startsWith("[")) return emptyList()

            // 3. 💥 ПАРСИМ СТРОКУ В СПИСОК СТРОК (Магия kotlinx.serialization!)
            jsonParser.decodeFromString<List<String>>(cleanJson)
        } catch (e: Exception) {
            println("AI_ERROR: Сбой сканирования библиотеки -> ${e.message}")
            emptyList()
        }
    }

    /*override suspend fun scanLibraryForGames(imageBytes: ByteArray): List<String> {
        return withContext(Dispatchers.IO) {
            try {
                println("GeminiHelper [AI Scanner]: Анализ скриншота библиотеки...")
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                val prompt = """
                    You are an OCR and gaming expert. Extract the names of all video games visible in this screenshot of a game library (Steam, PlayStation, Xbox, Mobile, etc.).
                    Ignore UI elements, hours played, achievement text, or friend lists. Extract JUST the game titles.
                    
                    Return EXACTLY AND ONLY a valid JSON array of strings. No markdown, no ```json tags.
                    Example of output:["The Witcher 3: Wild Hunt", "Cyberpunk 2077", "Brawl Stars"]
                """.trimIndent()

                val response = generativeModel.generateContent(
                    content {
                        image(bitmap)
                        text(prompt)
                    }
                )

                val rawText =
                    response.text?.replace("```json", "")?.replace("```", "")?.trim() ?: "[]"
                println("GeminiHelper [AI Scanner]: Найдено: $rawText")

                val type = object : com.google.gson.reflect.TypeToken<List<String>>() {}.type
                gson.fromJson<List<String>>(rawText, type) ?: emptyList()

            } catch (e: Exception) {
                println("GeminiHelper [AI Scanner Error]: ${e.message}")
                emptyList()
            }
        }
    }*/

    override suspend fun saveDifficultyToGlobalCache(
        gameId: Int,
        difficulty: AchievementDifficulty,
    ) {
        withContext(Dispatchers.IO) {
            try {
                supabase.from("global_games_cache").update(
                    {
                        set("ai_difficulty", difficulty.name)
                    }
                ) {
                    filter { eq("id", gameId) }
                }
                println(
                    "RAWG_CACHE 🧠 Оценка ИИ (${difficulty.name}) успешно добавлена в глобальный кэш!"
                )
            } catch (e: Exception) {
                println("RAWG_CACHE ❌ Ошибка сохранения оценки ИИ: ${e.message}")
            }
        }
    }

    override suspend fun verifyCustomChallenge(
        imageBytes: ByteArray,
        gameName: String,
        challengePrompt: String
    ): Boolean {
        return try {
            println("GeminiHelper [Custom Challenge]: Проверка челленджа для $gameName")

            val prompt = """
                You are a strict anti-cheat moderator for a video game tracking application.
                A user claims to have completed a custom challenge for the game "$gameName".
                
                YOUR OBJECTIVE:
                Analyze the provided screenshot and verify if the following condition is met:
                ---
                CHALLENGE CONDITION: "$challengePrompt"
                ---
                
                Look closely at UI elements, inventory, mods, timers, or character stats that match the condition.
                
                Return EXACTLY ONE WORD:
                TRUE - if the screenshot clearly proves the challenge condition is met.
                FALSE - if it does not prove it, or if it's unrelated.
            """.trimIndent()

            val responseText = geminiClient.analyzeImage(prompt, imageBytes)

            val answer = responseText.trim().uppercase()
            println("GeminiHelper [Custom Challenge]: Вердикт ИИ -> $answer")

            answer.contains("TRUE")

        }catch (e: Exception){
            println("GeminiHelper [Custom Challenge Error]: ${e.message}")
            false
        }
    }

    /*override suspend fun verifyCustomChallenge(
        imageBytes: ByteArray,
        gameName: String,
        challengePrompt: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                println("GeminiHelper [Custom Challenge]: Проверка челленджа для $gameName")
                val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)

                val prompt = """
                You are a strict anti-cheat moderator for a video game tracking application.
                A user claims to have completed a custom challenge for the game "$gameName".
                
                YOUR OBJECTIVE:
                Analyze the provided screenshot and verify if the following condition is met:
                ---
                CHALLENGE CONDITION: "$challengePrompt"
                ---
                
                Look closely at UI elements, inventory, mods, timers, or character stats that match the condition.
                
                Return EXACTLY ONE WORD:
                TRUE - if the screenshot clearly proves the challenge condition is met.
                FALSE - if it does not prove it, or if it's unrelated.
            """.trimIndent()

                val response = generativeModel.generateContent(content {
                    image(bitmap)
                    text(prompt)
                })

                val answer = response.text?.trim()?.uppercase() ?: "FALSE"
                println("GeminiHelper [Custom Challenge]: Вердикт ИИ -> $answer")

                answer.contains("TRUE")
            } catch (e: Exception) {
                println("GeminiHelper [Custom Challenge Error]: ${e.message}")
                false
            }
        }
    }*/
}
