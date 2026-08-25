package com.vdggrtf.playlog.data.network.response.gemini

import com.vdggrtf.playlog.data.network.dto.gemini.GeminiContent
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeminiResponse(
    @SerialName("candidates") val candidates: List<GeminiCandidate>? = null
)

@Serializable
data class GeminiCandidate(
    @SerialName("content") val content: GeminiContent? = null
)