package com.sgorinov.exilehelper.core.domain.usecases

import com.sgorinov.exilehelper.core.data.models.*
import com.sgorinov.exilehelper.core.domain.Repository
import com.sgorinov.exilehelper.core.presentation.models.InnerFilterData
import com.sgorinov.exilehelper.core.presentation.models.ItemFilterGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetSearchItemsListData(private val repository: Repository) {

    suspend operator fun invoke(filters: List<ItemFilterGroup>, league: String): List<String> {
        return withContext(Dispatchers.IO) {
            val mappedFilters = filters.filter { filter ->
                filter.checked.value
            }.mapNotNull { filterGroup ->
                val innerFilters = mapInnerFilters(filterGroup.filters)
                if (innerFilters.isNotEmpty()) {
                    Filter(
                        id = filterGroup.id,
                        filters = mapInnerFilters(filterGroup.filters)
                    )
                } else {
                    null
                }
            }

            val response = repository.itemsSearchList(
                data = SearchItemsRequest(
                    query = if (mappedFilters.isNotEmpty()) {
                        Query(filters = mappedFilters)
                    } else {
                        Query()
                    }
                ),
                league = league
            )

            response.result
        }
    }

    private fun mapInnerFilters(filters: List<InnerFilterData>): List<InnerFilters> {
        return filters.mapNotNull { filter ->
            val selectedOption = filter.selectedOption.value?.id

            val mappedInputs = filter.inputStates?.mapNotNull { input ->
                if (input.value.value.isNotBlank()) {
                    val numOrString = input.value.value.toIntOrNull() ?: input.value.value
                    input.key to numOrString
                } else {
                    null
                }
            }?.toMap<String, Any>()

            val inputFilters = when {
                mappedInputs?.isNotEmpty() == true && selectedOption != null -> {
                    mappedInputs.plus(mapOf("option" to selectedOption))
                }
                mappedInputs?.isNotEmpty() == true -> {
                    mappedInputs
                }
                !selectedOption.isNullOrBlank() -> {
                    mapOf("option" to selectedOption)
                }
                else -> null
            }

            if (inputFilters != null) {
                InnerFilters(filter.id, InputFilters(filters = inputFilters))
            } else {
                null
            }
        }
    }
}