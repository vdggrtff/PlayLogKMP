package com.vdggrtf.playlog.domain.model


data class AdvancedFilters(
    val ratingRange: ClosedFloatingPointRange<Float> = 0f..5f,
    val yearRange: ClosedFloatingPointRange<Float> = 1990f..2026f,
    val difficulty: AchievementDifficulty = AchievementDifficulty.NONE,
    val hasBounties: Boolean = false,
    val selectedGenres: List<String> = emptyList(),
    val selectedPlatforms: List<String> = emptyList(),
)
