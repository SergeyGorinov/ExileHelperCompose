package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class ItemsDataResponse(
    val result: List<ItemGroupData>
)

@Serializable
internal data class ItemGroupData(
    val id: String,
    val label: String,
    val entries: List<Item>
)

@Serializable
internal data class Item(
    val type: String,
    val text: String,
    val name: String? = null,
    val disc: String? = null,
    val flags: Flag? = null
)

@Serializable
internal data class Flag(
    val unique: Boolean? = null,
    val prophecy: Boolean? = null
)