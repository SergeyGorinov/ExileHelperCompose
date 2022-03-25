package com.sgorinov.exilehelper.core.data

import com.sgorinov.exilehelper.core.data.models.ItemsDataResponse
import com.sgorinov.exilehelper.core.data.models.StaticDataResponse
import retrofit2.http.GET

internal interface StaticApi {

    @GET("api/trade/data/static")
    suspend fun getStaticData(): StaticDataResponse

    @GET("api/trade/data/items")
    suspend fun getItemsData(): ItemsDataResponse
}