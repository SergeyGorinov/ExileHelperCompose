package com.sgorinov.exilehelper.core.domain

import com.sgorinov.exilehelper.core.data.models.SearchItemsRequest
import com.sgorinov.exilehelper.core.data.models.SearchItemsResponse
import com.sgorinov.exilehelper.core.domain.models.FiltersData
import com.sgorinov.exilehelper.core.domain.models.ItemGroup
import com.sgorinov.exilehelper.core.domain.models.StaticDataGroup

interface Repository {

    suspend fun staticData(): List<StaticDataGroup>

    suspend fun itemsData(): List<ItemGroup>

    suspend fun filtersData(): List<FiltersData>

    suspend fun itemsSearchList(data: SearchItemsRequest, league: String): SearchItemsResponse
}