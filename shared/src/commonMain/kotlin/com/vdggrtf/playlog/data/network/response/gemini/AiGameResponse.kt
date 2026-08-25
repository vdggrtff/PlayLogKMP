package com.vdggrtf.playlog.data.network.response.gemini

import com.vdggrtf.playlog.data.network.dto.gemini.AiGameRecommendationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AiGameResponse(
    @SerialName("recommendations") val recommendations: List<AiGameRecommendationDto>
)
