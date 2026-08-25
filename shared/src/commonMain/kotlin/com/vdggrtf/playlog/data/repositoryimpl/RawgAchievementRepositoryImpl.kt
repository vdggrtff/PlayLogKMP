package com.vdggrtf.playlog.data.repositoryimpl

import com.vdggrtf.playlog.data.network.api.RawgKtorClient
import com.vdggrtf.playlog.data.network.dto.rawg.AchievementDto
import com.vdggrtf.playlog.domain.repository.RawgAchievementRepository

class RawgAchievementRepositoryImpl(
    private val api: RawgKtorClient
): RawgAchievementRepository {
    override suspend fun getAchievementsByName(gameName: String): Result<List<AchievementDto>> {
        return try {
            val searchResponse = api.searchGameForId(gameName)
            val rawgGameId = searchResponse.results.firstOrNull()?.id ?: return Result.success(emptyList())

            val achResponse = api.getAchievements(rawgGameId)

            val mapped = achResponse.results.map {
                AchievementDto(
                    id = it.id,
                    name = it.name,
                    description = it.description,
                    image = it.image,
                    percent = it.percent.toDouble()
                )
            }

            println("RAWG_ACHIEVEMENTS: Украли ${mapped.size} ачивок для $gameName!")
            Result.success(mapped)
        } catch (e: Exception) {
            println("RAWG_ACHIEVEMENTS_ERROR: Ошибка кражи ачивок (${e.message})")
            // Если RAWG упал, мы не крашим прилу, а просто возвращаем пустой список
            Result.success(emptyList())
        }
    }
}