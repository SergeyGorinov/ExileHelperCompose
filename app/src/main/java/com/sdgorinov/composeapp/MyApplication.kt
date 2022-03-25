package com.sdgorinov.composeapp

import android.app.Application
import com.sgorinov.exilehelper.core.coreFeatureModule
import com.sgorinov.exilehelper.currency_feature.currencyFeatureModule

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        DI.init(
            this,
            mainModule + coreFeatureModule + currencyFeatureModule
        )
    }
}