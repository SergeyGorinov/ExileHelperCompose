package com.sdgorinov.filters_parser

import kotlinx.serialization.KSerializer

interface FiltersParser {

    fun parseFiltersFileUrl(rawHtmlData: String): String

    fun <T> parseFilters(filterRawData: String, kSerializer: KSerializer<T>): T
}