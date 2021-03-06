package com.sgorinov.exilehelper.currency_feature.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgorinov.exilehelper.core.domain.usecases.GetFilterData
import com.sgorinov.exilehelper.core.domain.usecases.GetStaticData
import com.sgorinov.exilehelper.core.presentation.models.ItemFilterGroup
import com.sgorinov.exilehelper.core.presentation.models.StaticGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CurrencyFeatureViewModel(
    private val getStaticDataUseCase: GetStaticData,
    private val getFilterDataUseCase: GetFilterData
) : ViewModel() {

    private val staticData = MutableStateFlow<List<StaticGroup>>(emptyList())
    private val filterData = MutableStateFlow<List<ItemFilterGroup>>(emptyList())

    val staticDataState: StateFlow<List<StaticGroup>> = staticData
    val filterDataState: StateFlow<List<ItemFilterGroup>> = filterData

    fun requestStaticData() = viewModelScope.launch(Dispatchers.IO) {
        staticData.emit(getStaticDataUseCase.execute())
    }

    fun requestFilterData() = viewModelScope.launch(Dispatchers.IO) {
//        filterData.emit(getFilterDataUseCase.execute())
    }
}