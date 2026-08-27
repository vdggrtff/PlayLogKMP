package com.vdggrtf.playlog.di

import org.koin.core.context.startKoin
import org.koin.core.module.Module

// Эта функция будет вызываться на старте приложения (в Android - в Application, на Desktop - в main)
fun initKoin(platformModule: Module) {
    startKoin {
        modules(
            networkModule,
            repositoryModule,
            domainModule,
            presentationModule,
            platformModule // Сюда мы передадим платформенные штуки (например, создание файла Room)
        )
    }
}