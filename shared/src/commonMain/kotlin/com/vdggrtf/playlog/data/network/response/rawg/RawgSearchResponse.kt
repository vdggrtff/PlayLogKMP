package com.vdggrtf.playlog.data.network.response.rawg

import com.vdggrtf.playlog.data.network.dto.rawg.RawgIdOnlyDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RawgSearchResponse(
    @SerialName("results") val results: List<RawgIdOnlyDto>
)