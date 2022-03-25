package com.sgorinov.exilehelper.core.presentation.models

class StaticGroup(
    val id: String,
    val label: String?,
    val entries: List<StaticGroupItem>
)

class StaticGroupItem(
    val id: String,
    val label: String,
    val imageUrl: String?
)