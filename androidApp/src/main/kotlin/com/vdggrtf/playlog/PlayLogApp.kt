package com.vdggrtf.playlog

import android.app.Application
import com.vdggrtf.playlog.di.createAndroidModule
import com.vdggrtf.playlog.di.initKoin

class PlayLogApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // 💥 ЗАПУСКАЕМ KOIN И ПЕРЕДАЕМ ЕМУ НАШУ БАЗУ И DATASTORE
        initKoin(
            platformModule = createAndroidModule(applicationContext)
        )
    }
}