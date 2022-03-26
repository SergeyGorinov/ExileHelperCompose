package com.sgorinov.exilehelper.core.data.models

import kotlinx.serialization.Serializable

@Serializable
data class SearchItemsResponse(
    val complexity: Int = 0,
    val id: String = "",
    val result: List<String> = emptyList(),
    val total: Int = 0,
    val inexact: Boolean = false
)