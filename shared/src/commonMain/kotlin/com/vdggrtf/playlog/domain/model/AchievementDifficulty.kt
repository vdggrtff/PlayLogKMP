package com.vdggrtf.playlog.domain.model

enum class AchievementDifficulty(val title: String, val emojiName: String) {

    NONE("None", "none"),
    EASY("Easy", "emoji_easy"),
    MEDIUM("Medium", "emoji_medium"),
    HARD("Hard", "emoji_hard"),
    DEMON("Demon", "emoji_demon"),
    IMPOSSIBLE("Impossible", "emoji_impossible"),

    CUSTOM_CHALLENGE("Custom_challenge", "emoji_custom_challenge"),
}