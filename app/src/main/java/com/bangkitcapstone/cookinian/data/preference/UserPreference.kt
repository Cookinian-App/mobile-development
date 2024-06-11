package com.bangkitcapstone.cookinian.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[NAME] = user.name
            preferences[AVATAR_URL] = user.avatarUrl
            preferences[EMAIL] = user.email
            preferences[TOKEN] = user.token
            preferences[IS_LOGIN] = true
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
               preferences[NAME] ?: "",
                preferences[AVATAR_URL] ?: "",
                preferences[EMAIL] ?: "",
                preferences[TOKEN] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    suspend fun editSession(newName: String) {
        dataStore.edit { preferences ->
            preferences[NAME] = newName
        }
    }

    suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.remove(NAME)
            preferences.remove(AVATAR_URL)
            preferences.remove(EMAIL)
            preferences.remove(TOKEN)
            preferences.remove(IS_LOGIN)
        }
    }

    suspend fun saveThemeMode(themeMode: String) {
        dataStore.edit { preferences ->
            preferences[THEME_MODE] = themeMode
        }
    }

    fun getThemeMode(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[THEME_MODE] ?: SYSTEM_DEFAULT
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME = stringPreferencesKey("name")
        private val AVATAR_URL = stringPreferencesKey("avatar_url")
        private val EMAIL = stringPreferencesKey("email")
        private val TOKEN = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")
        private val THEME_MODE = stringPreferencesKey("themeMode")

        const val SYSTEM_DEFAULT = "system_default"
        const val LIGHT_MODE = "light"
        const val DARK_MODE = "dark"

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}