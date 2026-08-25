package com.vdggrtf.playlog.data.network.dto.rawg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawgAchievDto(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("description") val description: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("percent") val percent: String = ""
)
