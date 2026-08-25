package com.vdggrtf.playlog.data.network.dto.gemini

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiGameRecommendationDto(
    @SerialName("gameName") val gameName: String,
    @SerialName("reason") val reason: String
)