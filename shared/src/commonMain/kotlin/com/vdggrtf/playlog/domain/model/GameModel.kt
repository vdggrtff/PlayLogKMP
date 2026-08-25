package com.vdggrtf.playlog.domain.model

data class GameModel(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val rating: Double?,
    val releasedDate: String?,
    val status: com.vdggrtf.playlog.domain.model.GameStatus,
    val unlockedAchievements: Int = 0,
    val totalAchievements: Int = 0,
    val descriptionRaw: String? = null,
    val playtime: Int = 0,
    val aiDifficulty: com.vdggrtf.playlog.domain.model.AchievementDifficulty = com.vdggrtf.playlog.domain.model.AchievementDifficulty.NONE,
    val userDifficulty: com.vdggrtf.playlog.domain.model.AchievementDifficulty = com.vdggrtf.playlog.domain.model.AchievementDifficulty.NONE,
    val verifiedDifficulty: com.vdggrtf.playlog.domain.model.AchievementDifficulty = com.vdggrtf.playlog.domain.model.AchievementDifficulty.NONE,
    val genres: List<String> = emptyList(),
    val platforms: List<String> = emptyList(),
)