package com.sdgorinov.composeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sgorinov.exilehelper.core.domain.usecases.GetFilterData
import com.sgorinov.exilehelper.core.domain.usecases.GetSearchItemsListData
import com.sgorinov.exilehelper.core.presentation.models.ItemFilterGroup
import com.sgorinov.exilehelper.core.presentation.models.StaticGroup
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

internal class MainViewModel(
//    private val getStaticDataUseCase: GetStaticData,
    private val getFilterDataUseCase: GetFilterData,
    private val getSearchItemsListData: GetSearchItemsListData
) : ViewModel() {

    private val staticData = MutableStateFlow<List<StaticGroup>>(emptyList())
    private val filterData = MutableStateFlow<List<ItemFilterGroup>>(emptyList())

    //    val staticDataState: StateFlow<List<StaticGroup>> = staticData
    val filterDataState: StateFlow<List<ItemFilterGroup>> = filterData

    init {
        requestFilterData()
    }

//    fun requestStaticData() = viewModelScope.launch(Dispatchers.IO) {
//        staticData.emit(getStaticDataUseCase.execute())
//    }

    fun onEvent(event: MainActivity.Event) {
        when (event) {
            MainActivity.Event.SearchItems -> {
                viewModelScope.launch {
                    val data = getSearchItemsListData(
                        filterDataState.value,
                        "Archnemesis"
                    )
                    println(data.firstOrNull())
                }
            }
        }
    }

    private fun requestFilterData() = viewModelScope.launch(Dispatchers.IO) {
        filterData.emit(getFilterDataUseCase.execute())
    }
}