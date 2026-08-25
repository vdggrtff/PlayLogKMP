package com.vdggrtf.playlog.data.network.response.rawg

import com.vdggrtf.playlog.data.network.dto.rawg.RawgAchievDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawgAchievementResponse(
    @SerialName("results") val results: List<RawgAchievDto>
)