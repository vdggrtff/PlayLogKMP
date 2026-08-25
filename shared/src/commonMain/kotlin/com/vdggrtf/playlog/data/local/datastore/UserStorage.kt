package com.vdggrtf.playlog.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserStorage (
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val LAST_SCREEN = stringPreferencesKey("last_screen")
        val USER_TOKEN = stringPreferencesKey("user_token")

        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    val userEmail: Flow<String > = dataStore.data.map { it[USER_EMAIL] ?: "" }

    val userName: Flow<String> =dataStore.data.map { it[USER_NAME] ?: "Gamer" }

    val lastScreen: Flow<String?> =dataStore.data.map { it[LAST_SCREEN] }

    val userToken: Flow<String?> =dataStore.data.map { it[USER_TOKEN] }

    val refreshToken: Flow<String?> =dataStore.data.map { it[REFRESH_TOKEN] }

    suspend fun saveToken(accessToken: String, refreshToken: String) {
        dataStore.edit {
            it[USER_TOKEN] = accessToken
            it[REFRESH_TOKEN] = refreshToken
        }
    }

    suspend fun saveUserData(name: String, email: String) {
        dataStore.edit { prefs ->
            prefs[USER_NAME] = name
            prefs[USER_EMAIL] = email
        }
    }

    suspend fun saveLastScreen(route: String) {
        dataStore.edit { it[LAST_SCREEN] = route }
    }

    suspend fun clearStorage() {
        dataStore.edit { it.clear() }
    }
}