package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class SearchItemsRequest(
    @Required
    val query: Query = Query(),
    @Required
    val sort: Sort = Sort()
)

@Serializable
data class Query(
    @Required
    val status: Status = Status(),
    @Required
    val stats: List<StatGroup> = listOf(StatGroup()),
    @Serializable(with = FiltersSerializer::class)
    val filters: List<Filter>? = null
)

@Serializable(with = FilterSerializer::class)
data class Filter(
    val id: String,
    val filters: List<InnerFilters> = emptyList()
)

@Serializable
data class InnerFilters(
    val id: String,
    val inputFilters: InputFilters = InputFilters()
)

@Serializable(with = InputFiltersSerializer::class)
data class InputFilters(
    val filters: Map<String, Any> = emptyMap()
)

@Serializable
data class StatGroup(
    @Required
    val type: String = "and",
    @Required
    val filters: List<Stat> = emptyList()
)

@Serializable
data class Stat(
    val id: String,
    val value: StatValue = StatValue(),
    val disabled: Boolean = false
)

@Serializable
data class StatValue(
    val min: Int? = null,
    val max: Int? = null,
    val option: Int? = null
)

@Serializable
data class Status(
    @Required
    val option: String = "online"
)

@Serializable
data class Sort(
    @Required
    val price: String = "asc"
)

private object FiltersSerializer : KSerializer<List<Filter>> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(Filter::class.java.simpleName)

    override fun deserialize(decoder: Decoder): List<Filter> {
        return emptyList() // Dummy
    }

    override fun serialize(encoder: Encoder, value: List<Filter>) {
        val jsonEncoder = encoder as JsonEncoder
        val filters = value.associate { filter ->
            filter.id to jsonEncoder.json.encodeToJsonElement(filter)
        }
        jsonEncoder.encodeJsonElement(JsonObject(filters))
    }
}

private object FilterSerializer : KSerializer<Filter> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(Filter::class.java.simpleName)

    override fun deserialize(decoder: Decoder): Filter {
        return Filter("") // Dummy
    }

    override fun serialize(encoder: Encoder, value: Filter) {
        val jsonEncoder = encoder as JsonEncoder
        val filters = value.filters.associate { filter ->
            filter.id to jsonEncoder.json.encodeToJsonElement(filter.inputFilters)
        }
        jsonEncoder.encodeJsonElement(JsonObject(mapOf("filters" to JsonObject(filters))))
    }
}

private object InputFiltersSerializer : KSerializer<InputFilters> {

    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor(InputFilters::class.java.simpleName)

    override fun deserialize(decoder: Decoder): InputFilters {
        return InputFilters() // Dummy
    }

    @Suppress("UNCHECKED_CAST")
    override fun serialize(encoder: Encoder, value: InputFilters) {
        val jsonEncoder = encoder as JsonEncoder
        val filters = value.filters.mapNotNull { (key, value) ->
            val serializer = when (value) {
                is Int -> Int.serializer()
                is String -> String.serializer()
                else -> return@mapNotNull null
            } as KSerializer<Any>
            val encodedValue = jsonEncoder.json.encodeToJsonElement(serializer, value)
            key to encodedValue
        }.toMap()
        jsonEncoder.encodeJsonElement(JsonObject(filters))
    }
}