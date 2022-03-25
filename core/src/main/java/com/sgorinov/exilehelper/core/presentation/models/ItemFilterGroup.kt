package com.sgorinov.exilehelper.core.presentation.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ItemFilterGroup(
    val id: String,
    val title: String,
    val filters: List<InnerFilterData>,
    val expanded: MutableState<Boolean> = mutableStateOf(true),
    val checked: MutableState<Boolean> = mutableStateOf(true)
)

data class InnerFilterData(
    val id: String,
    val text: String,
    val options: List<FilterOptionData>? = null,
    val isMinMax: Boolean = false,
    val minState: MutableState<String> = mutableStateOf(""),
    val maxState: MutableState<String> = mutableStateOf(""),
    val isSockets: Boolean = false
)

data class FilterOptionData(
    val id: String,
    val text: String
)