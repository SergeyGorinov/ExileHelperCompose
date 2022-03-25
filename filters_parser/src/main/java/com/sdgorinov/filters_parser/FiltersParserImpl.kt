package com.sdgorinov.filters_parser

import com.sdgorinov.filters_parser.models.FiltersHtmlData
import kotlinx.serialization.KSerializer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

internal class FiltersParserImpl : FiltersParser {

    private val baseUrlRegex = """baseUrl\s*:\s*"[\w\W]+",\n""".toRegex()
    private val tradeHashRegex = """paths\s*:\s*\{[\w\W]+\},\n""".toRegex()

    private val jsonParser = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    override fun parseFiltersFileUrl(rawHtmlData: String): String {
        val baseUrlValue = baseUrlRegex.find(rawHtmlData)?.value
        val tradeHashValue = tradeHashRegex.find(rawHtmlData)?.value
        val baseUrl = baseUrlValue
            ?.removeRange(0, baseUrlValue.indexOf(":") + 1)
            ?.trim()
            ?.removeSuffix(",")
            ?.removeSurrounding("\"")
        val tradeHashData = tradeHashValue
            ?.removeRange(0, tradeHashValue.indexOf(":") + 1)
            ?.trim()
            ?.removeSuffix(",")
        val tradeHash = if (tradeHashData != null) {
            jsonParser.decodeFromString<FiltersHtmlData>(tradeHashData).trade
        } else {
            null
        }
        return "$baseUrl${tradeHash}.js"
    }

    override fun <T> parseFilters(filterRawData: String, kSerializer: KSerializer<T>): T {
        val filterFuncStartIndex = filterRawData.indexOf("PoE/Trade/Data/Static")
        val filterFuncReturnStart = filterRawData.indexOf("return", filterFuncStartIndex)
        val filterFuncReturnEnd = filterRawData.indexOf("}),", filterFuncReturnStart)

        val filterFuncData = filterRawData
            .substring(filterFuncReturnStart, filterFuncReturnEnd)
            .trim()
            .removePrefix("return")
            .replace("\")", "\"")
            .replace("e.translate(", "")
            .replace("!0", "-1")

        return jsonParser.decodeFromString(kSerializer, filterFuncData)
    }
}