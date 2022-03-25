package com.sgorinov.exilehelper.core.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sgorinov.exilehelper.core.domain.Repository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.bind
import org.koin.dsl.module
import retrofit2.Retrofit

internal val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://www.pathofexile.com/")
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(StaticApi::class.java)
    } bind StaticApi::class

    single {
        OkHttpClient()
    }

    single {
        RepositoryImpl(api = get(), client = get(), filtersParser = get())
    } bind Repository::class
}