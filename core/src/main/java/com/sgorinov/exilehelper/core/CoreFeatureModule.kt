package com.sgorinov.exilehelper.core

import com.sdgorinov.filters_parser.filtersParserModule
import com.sgorinov.exilehelper.core.data.dataModule
import com.sgorinov.exilehelper.core.domain.domainModule

val coreFeatureModule = listOf(dataModule, domainModule) + filtersParserModule