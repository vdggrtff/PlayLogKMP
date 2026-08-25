package com.vdggrtf.playlog.data.network.dto.rawg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawgIdOnlyDto(
    @SerialName("id") val id: Int
)