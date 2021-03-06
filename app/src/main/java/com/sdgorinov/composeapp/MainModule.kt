package com.sdgorinov.composeapp

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {
    viewModel { MainViewModel(getFilterDataUseCase = get(), getSearchItemsListData = get()) }
}