package com.sgorinov.exilehelper.core.data

import com.sdgorinov.filters_parser.FiltersParser
import com.sgorinov.exilehelper.core.data.models.Item
import com.sgorinov.exilehelper.core.data.models.StaticDataResponseGroupItem
import com.sgorinov.exilehelper.core.domain.Repository
import com.sgorinov.exilehelper.core.domain.models.*
import okhttp3.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

internal class RepositoryImpl(
    private val api: StaticApi,
    private val client: OkHttpClient,
    private val filtersParser: FiltersParser
) : Repository {

    private var cachedStaticData = emptyList<StaticDataGroup>()
    private var cachedItemsData = emptyList<ItemGroup>()
    private var cachedFiltersData = emptyList<FiltersData>()

    override suspend fun staticData(): List<StaticDataGroup> {
        if (cachedStaticData.isEmpty()) {
            cachedStaticData = api.getStaticData().result.map {
                StaticDataGroup(it.id, it.label, mapStaticDataEntries(it.entries))
            }
        }
        return cachedStaticData
    }

    override suspend fun itemsData(): List<ItemGroup> {
        if (cachedItemsData.isEmpty()) {
            cachedItemsData = api.getItemsData().result.map {
                ItemGroup(it.id, it.label, mapItemsDataEntries(it.entries))
            }
        }
        return cachedItemsData
    }

    override suspend fun filtersData(): List<FiltersData> {
        if (cachedFiltersData.isEmpty()) {
            val request = Request.Builder()
                .url("https://www.pathofexile.com/trade")
                .get()
                .build()

            val htmlData: String =
                client.newCall(request).asSuspendCoroutine() ?: throw IOException("No script data")

            val filtersFileUrl = filtersParser.parseFiltersFileUrl(htmlData)

            val rawFiltersData =
                getRawFilterData(filtersFileUrl) ?: throw IOException("No filter raw data")

            cachedFiltersData = filtersParser.parseFilters(
                rawFiltersData,
                FiltersFuncData.serializer()
            ).propertyFilters
        }

        return cachedFiltersData
    }

    private suspend fun getRawFilterData(url: String): String? {
        val request = Request.Builder()
            .url(url)
            .get()
            .build()

        return client.newCall(request).asSuspendCoroutine()
    }

    private fun mapStaticDataEntries(entries: List<StaticDataResponseGroupItem>): List<StaticDataGroupItem> {
        return entries.map { StaticDataGroupItem(it.id, it.text, it.image) }
    }

    private fun mapItemsDataEntries(entries: List<Item>): List<ItemData> {
        return entries.map {
            ItemData(
                type = it.type,
                text = it.text,
                name = it.name,
                disc = it.disc,
                flags = ItemFlag(it.flags?.unique, it.flags?.prophecy)
            )
        }
    }

    private suspend inline fun <reified T> Call.asSuspendCoroutine(): T? {
        return suspendCoroutine { continuation ->
            enqueue(
                object : Callback {
                    override fun onResponse(call: Call, response: Response) {
                        val data = response.body?.string()
                        if (data is T) {
                            continuation.resume(data)
                        } else {
                            continuation.resume(null)
                        }
                    }

                    override fun onFailure(call: Call, e: IOException) {
                        continuation.resumeWithException(e)
                    }
                }
            )
        }
    }
}