package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Serializable

@Serializable
internal data class StaticDataResponse(
    val result: List<StaticDataResponseGroup>
)

@Serializable
internal data class StaticDataResponseGroup(
    val id: String,
    val label: String? = null,
    val entries: List<StaticDataResponseGroupItem>
)

@Serializable
internal data class StaticDataResponseGroupItem(
    val id: String,
    val text: String,
    val image: String? = null
)