package com.vdggrtf.playlog.data.network.dto.cheapshark

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CheapSharkDealDto(
    @SerialName("title") val title: String,
    @SerialName("salePrice") val salePrice: String,
    @SerialName("normalPrice") val normalPrice: String,
    @SerialName("storeID") val storeId: String,
)
