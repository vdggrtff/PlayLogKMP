package com.vdggrtf.playlog.domain.usecase.main.game

import com.vdggrtf.playlog.domain.model.AchievementDifficulty
import com.vdggrtf.playlog.domain.repository.AiRepository
import com.vdggrtf.playlog.domain.repository.GameRepository

class RetryAiEvaluationUseCase (
    private val aiRepository: AiRepository,
    private val gameRepository: GameRepository,
) {

    suspend operator fun invoke(gameId: Int, gameName: String): Result<AchievementDifficulty> {
        return try {
            println("GameDetailsVM 🔄 Пинаем ИИ заново для игры $gameName...")

            val cached = gameRepository.getCachedGame(gameId)
            val cachedAchivs = cached?.achievements ?: emptyList()

            val aiDiff = aiRepository.evaluateGameDifficulty(gameName, cachedAchivs)
            if (aiDiff != AchievementDifficulty.NONE) {
                val cached = gameRepository.getCachedGame(gameId)
                if (cached != null) {
                    gameRepository.saveToCache(cached.copy(aiDifficulty = aiDiff.name))
                    println("RAWG_CACHE✅ Успешно обновили сложность в кэше на ${aiDiff.name}")
                }
            }
            Result.success(aiDiff)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}