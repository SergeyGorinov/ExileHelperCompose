package com.sgorinov.exilehelper.core.data

import com.sgorinov.exilehelper.core.data.models.ItemsDataResponse
import com.sgorinov.exilehelper.core.data.models.SearchItemsRequest
import com.sgorinov.exilehelper.core.data.models.SearchItemsResponse
import com.sgorinov.exilehelper.core.data.models.StaticDataResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

internal interface StaticApi {

    @GET("api/trade/data/static")
    suspend fun getStaticData(): StaticDataResponse

    @GET("api/trade/data/items")
    suspend fun getItemsData(): ItemsDataResponse

    @POST("api/trade/search/{league}")
    suspend fun requestSearchItemsList(
        @Path("league") league: String,
        @Body body: SearchItemsRequest
    ): SearchItemsResponse

}