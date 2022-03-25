package com.sgorinov.exilehelper.currency_feature.presentation

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

internal val presentationModule = module {
    viewModel { CurrencyFeatureViewModel(get(), get()) }
}