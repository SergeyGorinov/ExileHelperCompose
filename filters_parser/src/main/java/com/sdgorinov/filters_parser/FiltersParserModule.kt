package com.sdgorinov.filters_parser

import org.koin.dsl.bind
import org.koin.dsl.module

val filtersParserModule = module {
    single { FiltersParserImpl() } bind FiltersParser::class
}