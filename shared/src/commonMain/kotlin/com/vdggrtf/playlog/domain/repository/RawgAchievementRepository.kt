package com.vdggrtf.playlog.domain.repository

import com.vdggrtf.playlog.data.network.dto.rawg.AchievementDto

interface RawgAchievementRepository {

    suspend fun getAchievementsByName(gameName: String): Result<List<AchievementDto>>
}