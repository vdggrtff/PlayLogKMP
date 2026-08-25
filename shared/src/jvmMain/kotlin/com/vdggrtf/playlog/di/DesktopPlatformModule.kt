package com.vdggrtf.playlog.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.vdggrtf.playlog.data.local.dao.GameDao
import com.vdggrtf.playlog.data.local.dao.PlaylistDao
import com.vdggrtf.playlog.data.local.datastore.UserStorage
import com.vdggrtf.playlog.data.local.db.AppDataBase
import com.vdggrtf.playlog.utils.DesktopShareManager
import com.vdggrtf.playlog.utils.ShareManager
import okio.Path.Companion.toOkioPath
import org.koin.dsl.module
import java.io.File

fun createDesktopModule() = module {

    // 1. DATASTORE (Файл настроек)
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                // Создаем файл прямо в домашней папке пользователя на ПК (C:\Users\Name или /home/name/)
                File(System.getProperty("user.home"), "playlog_prefs.preferences_pb").toOkioPath()
            }
        )
    }

    single { UserStorage(dataStore = get()) }

    // 2. ROOM DATABASE (Файл базы данных)
    single<AppDataBase> {
        val dbFile = File(System.getProperty("user.home"), "playlog_database.db")

        // В Desktop нет Context, поэтому база создается просто по пути к файлу!
        Room.databaseBuilder<AppDataBase>(dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver()) // Тот же мультиплатформенный драйвер SQLite
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    // 3. DAOs
    single<GameDao> { get<AppDataBase>().gameDao() }
    single<PlaylistDao> { get<AppDataBase>().playlistDao() }

    single<ShareManager> { DesktopShareManager() }
}