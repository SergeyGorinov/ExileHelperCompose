package com.sgorinov.exilehelper.core.domain

import com.sgorinov.exilehelper.core.domain.usecases.GetFilterData
import com.sgorinov.exilehelper.core.domain.usecases.GetSearchItemsListData
import com.sgorinov.exilehelper.core.domain.usecases.GetStaticData
import org.koin.dsl.module

internal val domainModule = module {
    single { GetStaticData(repository = get()) }
    single { GetFilterData(repository = get()) }
    single { GetSearchItemsListData(repository = get()) }
}