package com.vdggrtf.playlog.data.network.dto.rawg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AchievementDto(
    @SerialName("id") val id: Int? = null,
    @SerialName("name") val name: String? = null,
    @SerialName("description") val description: String? = null,
    @SerialName("image") val image: String? = null,
    @SerialName("percent") val percent: Double? = null,
)