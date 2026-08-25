package com.vdggrtf.playlog.data.network.dto.cheapshark

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheapSharkGameDto(
    @SerialName("gameID") val gameId: String,
    @SerialName("external") val name: String,
    @SerialName("cheapest") val cheapestPrice: String,
    @SerialName("cheapestDealID") val dealId: String,
)