package com.sgorinov.exilehelper.core.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class FiltersFuncData(
    val propertyFilters: List<FiltersData>
)

@Serializable
data class FiltersData(
    val id: String,
    val title: String,
    val filters: List<FilterData>
)

@Serializable
data class FilterData(
    val id: String,
    val text: String,
    val option: Options? = null,
    val minMax: Int? = null,
    val tip: String? = null,
    val sockets: Int? = null,
    val input: Input? = null
)

@Serializable
data class Options(
    val options: List<Option>? = null,
    val knownItem: KnownItems? = null
)

@Serializable
data class Option(
    val id: String?,
    val text: String
)

@Serializable
data class KnownItems(
    val uniques: Int? = null,
    val cards: Int? = null,
    val currency: Int? = null
)

@Serializable
data class Input(
    val placeholder: String? = null
)