package com.vdggrtf.playlog.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.vdggrtf.playlog.data.local.dao.GameDao
import com.vdggrtf.playlog.data.local.dao.PlaylistDao
import com.vdggrtf.playlog.data.local.datastore.UserStorage
import com.vdggrtf.playlog.data.local.db.AppDataBase
import com.vdggrtf.playlog.utils.AndroidShareManager
import com.vdggrtf.playlog.utils.ShareManager
import okio.Path.Companion.toPath
import org.koin.dsl.module
import java.io.File

fun createAndroidModule(context: Context) = module {

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.createWithPath(
            produceFile = {
                // 💥 ПРАВИЛЬНЫЙ ПУТЬ БЕЗ toOkioPath!
                File(context.filesDir, "playlog_prefs.preferences_pb").absolutePath.toPath()
            }
        )
    }

    single { UserStorage(dataStore = get()) }

    // 2. ROOM DATABASE
    single<AppDataBase> {
        val dbFile = context.getDatabasePath("playlog_database.db")
        Room.databaseBuilder<AppDataBase>(context, dbFile.absolutePath)
            .setDriver(BundledSQLiteDriver())
            .fallbackToDestructiveMigration(dropAllTables = true)
            .build()
    }

    // 3. DAOs
    single<GameDao> { get<AppDataBase>().gameDao() }
    single<PlaylistDao> { get<AppDataBase>().playlistDao() }

    single<ShareManager> { AndroidShareManager(context = get()) }

}