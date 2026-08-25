package com.vdggrtf.playlog.domain.usecase.main.game

import com.vdggrtf.playlog.data.mapper.toDomainModel
import com.vdggrtf.playlog.data.network.dto.supabase.challenges.CashedGameDto
import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.domain.model.GameModel
import com.vdggrtf.playlog.domain.model.GameStatus
import com.vdggrtf.playlog.domain.model.RemoteGameData
import com.vdggrtf.playlog.domain.repository.AiRepository
import com.vdggrtf.playlog.domain.repository.GameRepository
import com.vdggrtf.playlog.domain.repository.RawgAchievementRepository
import com.vdggrtf.playlog.domain.repository.RetroAchievementsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class FetchAndSyncRemoteGameUseCase(
    private val gameRepository: GameRepository,
    private val aiRepository: AiRepository,
    private val raRepository: RetroAchievementsRepository,
    private val rawgAchievementRepository: RawgAchievementRepository,
) {
    suspend operator fun invoke(id: Int, localGame: GameModel?): Result<RemoteGameData> {
        return try {
            // 1. ПЫТАЕМСЯ ВЗЯТЬ ИЗ ГЛОБАЛЬНОГО КЭША (SUPABASE)
            val cachedDto = gameRepository.getCachedGame(id)
            if (cachedDto != null) {
                println("RAWG_CACHE 🔥 HIT! ВЗЯЛИ ВСЁ ИЗ КЭША!")
                val cachedGameModel = cachedDto.toDomainModel()

                val aiDiff = try {
                    cachedDto.aiDifficulty?.let { AchievementDifficulty.valueOf(it) }
                        ?: AchievementDifficulty.NONE
                } catch (e: Exception) {
                    AchievementDifficulty.NONE
                }

                // SMART MERGE #1
                val finalGame = cachedGameModel.copy(
                    status = localGame?.status ?: GameStatus.NONE,
                    aiDifficulty = localGame?.aiDifficulty ?: aiDiff,
                    userDifficulty = localGame?.userDifficulty ?: AchievementDifficulty.NONE,
                    verifiedDifficulty = localGame?.verifiedDifficulty ?: AchievementDifficulty.NONE
                )

                var finalAiDiff = localGame?.aiDifficulty ?: aiDiff

                // Пинаем ИИ, если оценки еще нет
                if (finalAiDiff == AchievementDifficulty.NONE) {
                    try {
                        val cachedAchivs = cachedDto.achievements ?: emptyList()
                        finalAiDiff = aiRepository.evaluateGameDifficulty(finalGame.name, cachedAchivs)
                        if (finalAiDiff != AchievementDifficulty.NONE) {
                            gameRepository.saveToCache(cachedDto.copy(aiDifficulty = finalAiDiff.name))
                        }
                    } catch (e: Exception) { /* Игнорируем ошибку ИИ */
                    }
                }

                return Result.success(
                    RemoteGameData(
                        game = finalGame,
                        screenshots = cachedDto.screenshots ?: emptyList(),
                        achievements = cachedDto.achievements ?: emptyList(),
                        objectiveDifficulty = finalAiDiff
                    )
                )
            }
            // 2. КЭША НЕТ. ИДЕМ В RAWG API
            println("RAWG_CACHE 🧊 MISS! Качаем из RAWG...")
            coroutineScope {
                val detailsDef = async { gameRepository.getGameDetails(id) }
                val screensDef = async { gameRepository.getScreenshots(id) }

                val detailsResult = detailsDef.await()
                val screens = screensDef.await().getOrNull() ?: emptyList()

                if (detailsResult.isSuccess) {
                    val networkGame = detailsResult.getOrNull()!!

                    val rawgDef = async { rawgAchievementRepository.getAchievementsByName(networkGame.name) }
                    val raDef = async { raRepository.getRetroAchievements(networkGame.name, networkGame.platforms) }

                    val rawgResult = rawgDef.await()
                    val raResult = raDef.await()

                    val rawgAchivs = rawgResult.getOrDefault(emptyList())
                    val raAchivs = raResult.getOrDefault(emptyList())

                    /*if (raAchivs.isNotEmpty()) {
                        println("RetroAchievements 🔥 Склеиваем ${achivs.size} RAWG ачивок и ${raAchivs.size} Ретро-ачивок!")
                        achivs = achivs + raAchivs
                    }*/

                    val totalAchivs = rawgAchivs + raAchivs

                    if (totalAchivs.isNotEmpty()) {
                        println("Achievements: 🔥 Склеили ${rawgAchivs.size} RAWG и ${raAchivs.size} Ретро-ачивок!")
                    }


                    // SMART MERGE #2
                    val mergedGame = networkGame.copy(
                        status = localGame?.status ?: GameStatus.NONE,
                        aiDifficulty = localGame?.aiDifficulty ?: AchievementDifficulty.NONE,
                        userDifficulty = localGame?.userDifficulty ?: AchievementDifficulty.NONE,
                        verifiedDifficulty = localGame?.verifiedDifficulty
                            ?: AchievementDifficulty.NONE
                    )

                    var aiDiff = AchievementDifficulty.NONE
                    try {
                        aiDiff = aiRepository.evaluateGameDifficulty(networkGame.name, totalAchivs)
                        // Сохраняем в кэш!
                        val cacheDto = CashedGameDto(
                            id = networkGame.id,
                            name = networkGame.name,
                            imageUrl = networkGame.imageUrl,
                            releasedDate = networkGame.releasedDate,
                            rating = networkGame.rating,
                            description = networkGame.descriptionRaw,
                            screenshots = screens,
                            achievements = totalAchivs,
                            aiDifficulty = if (aiDiff == AchievementDifficulty.NONE) null else aiDiff.name,
                            genres = networkGame.genres.joinToString(","),
                            platforms = networkGame.platforms.joinToString(",")
                        )
                        gameRepository.saveToCache(cacheDto)
                    } catch (e: Exception) {
                        println("RAWG_CACHE ❌ Ошибка кэширования: ${e.message}")
                    }

                    Result.success(
                        RemoteGameData(
                            mergedGame,
                            screens,
                            totalAchivs,
                            aiDiff
                        )
                    )
                } else {
                    Result.failure(
                        Exception(
                            detailsResult.exceptionOrNull()?.message ?: "Unknown RAWG Error"
                        )
                    )
                }
            }
        } catch (e: Exception) {
            println("GameDetails ФАТАЛЬНАЯ ОШИБКА: ${e.message}")
            Result.failure(e)
        }
    }
}