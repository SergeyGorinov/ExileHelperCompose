package com.sgorinov.exilehelper.core.domain.models

data class StaticDataGroup(
    val id: String,
    val label: String?,
    val entries: List<StaticDataGroupItem>
)

data class StaticDataGroupItem(
    val id: String,
    val label: String,
    val imageUrl: String?
)
