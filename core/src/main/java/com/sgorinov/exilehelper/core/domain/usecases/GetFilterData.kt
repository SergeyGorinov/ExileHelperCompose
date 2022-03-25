package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.domain.Repository
import com.sgorinov.exilehelper.core.domain.models.FilterData
import com.sgorinov.exilehelper.core.domain.models.Options
import com.sgorinov.exilehelper.core.presentation.models.FilterOptionData
import com.sgorinov.exilehelper.core.presentation.models.InnerFilterData
import com.sgorinov.exilehelper.core.presentation.models.ItemFilterGroup

class GetFilterData(private val repository: Repository) {

    suspend fun execute(): List<ItemFilterGroup> {
        return repository.filtersData().map { filterData ->
            ItemFilterGroup(
                id = filterData.id,
                title = filterData.title,
                filters = mapInnerFilters(filterData.filters)
            )
        }
    }

    private suspend fun mapInnerFilters(filters: List<FilterData>): List<InnerFilterData> {
        return filters.map {
            InnerFilterData(
                id = it.id,
                text = it.text,
                options = mapOptions(it.options),
                isMinMax = it.minMax != null,
                isSockets = it.sockets != null
            )
        }
    }

    private suspend fun mapOptions(options: Options?): List<FilterOptionData>? {
        return when {
            options?.options != null -> {
                options.options.map {
                    FilterOptionData(id = it.id, text = it.text)
                }
            }
            options?.knownItem != null -> {
                val resultList = mutableListOf<FilterOptionData>()
                if (options.knownItem.uniques != null) {
                    resultList.addAll(
                        repository.itemsData()
                            .flatMap { it.entries }
                            .filter { it.flags?.unique == true }
                            .map { FilterOptionData(id = it.text, text = it.text) }
                    )
                }
                if (options.knownItem.cards != null) {
                    resultList.addAll(
                        repository.itemsData()
                            .firstOrNull { it.id == CARDS_ID }?.entries
                            ?.map { FilterOptionData(it.text, it.text) }
                            ?: emptyList()
                    )
                }
                if (options.knownItem.currency != null) {
                    resultList.addAll(
                        repository.staticData()
                            .firstOrNull { it.id == CURRENCY_ID }?.entries
                            ?.map { FilterOptionData(it.id, it.label) }
                            ?: emptyList()
                    )
                }
                resultList
            }
            else -> null
        }
    }

    private companion object {
        const val CARDS_ID = "cards"
        const val CURRENCY_ID = "Currency"
    }
}