package com.sgorinov.exilehelper.core.domain

import com.sgorinov.exilehelper.core.domain.usecases.GetFilterData
import com.sgorinov.exilehelper.core.domain.usecases.GetStaticData
import org.koin.dsl.module

internal val domainModule = module {
    single { GetStaticData(get()) }
    single { GetFilterData(get()) }
}