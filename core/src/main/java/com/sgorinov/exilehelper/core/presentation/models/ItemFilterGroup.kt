package com.sgorinov.exilehelper.core.presentation.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ItemFilterGroup(
    val id: String,
    val title: String,
    val filters: List<InnerFilterData>,
    val expanded: MutableState<Boolean> = mutableStateOf(true),
    val checked: MutableState<Boolean> = mutableStateOf(true)
)

data class InnerFilterData(
    val id: String,
    val title: String,
    val placeholder: String,
    val options: List<FilterOptionData>? = null,
    val isMinMax: Boolean = false,
    val isSockets: Boolean = false
) {
    private val minMaxPlaceholders = listOf("min", "max")
    private val socketsPlaceholders = listOf("r", "g", "b", "w")

    val inputStates: Map<String, MutableState<String>>? = when {
        isMinMax && isSockets -> {
            (minMaxPlaceholders + socketsPlaceholders).associateWith { mutableStateOf("") }
        }
        isMinMax -> {
            minMaxPlaceholders.associateWith { mutableStateOf("") }
        }
        isSockets -> {
            socketsPlaceholders.associateWith { mutableStateOf("") }
        }
        id == "account" -> {
            mapOf(placeholder to mutableStateOf(""))
        }
        else -> null
    }

    val selectedOption = if (options != null) mutableStateOf(options.first()) else null
}

data class FilterOptionData(
    val id: String?,
    val text: String
)