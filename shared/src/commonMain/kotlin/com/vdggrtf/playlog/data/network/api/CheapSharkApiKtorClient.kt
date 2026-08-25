package com.vdggrtf.playlog.data.network.api

import com.vdggrtf.playlog.data.network.dto.cheapshark.CheapSharkDealDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class CheapSharkApiKtorClient(
    private val httpClient: HttpClient, // Наш движок
) {

    suspend fun getStoreSpecificDeals(
        title: String,
        storeIds: String = "7,11",
        sortBy: String = "Price",
        limit: Int = 1
    ): List<CheapSharkDealDto>{
        return httpClient.get("https://www.cheapshark.com/api/1.0/deals") {
            header("User-Agent", "PlayLog/1.0 (playlogandroidapp@gmail.com)")

            parameter("title", title)
            parameter("storeID", storeIds)
            parameter("sortBy", sortBy)
            parameter("limit", limit)
        }.body()
    }
}