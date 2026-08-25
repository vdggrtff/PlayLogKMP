package com.vdggrtf.playlog.data.network.dto.retro_achievements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaAchievementDto(
    @SerialName("ID") val id: Int,
    @SerialName("Title") val title: String,
    @SerialName("Description") val description: String,
    @SerialName("Points") val points: Int,
    @SerialName("BadgeName") val badgeName: String
)