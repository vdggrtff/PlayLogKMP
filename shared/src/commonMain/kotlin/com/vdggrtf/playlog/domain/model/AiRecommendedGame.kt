package com.vdggrtf.playlog.domain.model

data class AiRecommendedGame(
    val aiReason: String,
    val gameDetails: GameModel?,
)