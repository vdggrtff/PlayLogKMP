package com.vdggrtf.playlog.data.network.dto.retro_achievements

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RaGameDto(
    @SerialName("ID") val id: Int,
    @SerialName("Title") val title: String,
    @SerialName("ConsoleID") val consoleId: Int
)