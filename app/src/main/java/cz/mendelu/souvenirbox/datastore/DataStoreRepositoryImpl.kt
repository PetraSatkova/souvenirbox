package cz.mendelu.souvenirbox.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreRepositoryImpl(private val context: Context) : IDataStoreRepository {

    override val darkThemeFlow: Flow<Boolean> =
        context.dataStore.data
            .map { prefs -> prefs[booleanPreferencesKey(DataStoreConstants.DARK_THEME)] ?: false }


    override suspend fun setDarkTheme(value: Boolean) {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.DARK_THEME)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = value
        }
    }

    override suspend fun setLanguage(value: String) {
//        val preferencesKey = stringPreferencesKey(DataStoreConstants.LANGUAGE)
    }

    override suspend fun getLanguage(): String {
        TODO("Not yet implemented")
    }

    override suspend fun setCurrency(value: String) {
        TODO("Not yet implemented")
    }

    override suspend fun getCurrency(): String {
        TODO("Not yet implemented")
    }

    override suspend fun setLoginSuccessful() {
        val preferencesKey = booleanPreferencesKey(DataStoreConstants.LOGIN_SUCCESSFUL)
        context.dataStore.edit { preferences ->
            preferences[preferencesKey] = true
        }
    }

    override suspend fun getLoginSuccessful(): Boolean {
        return try {
            val preferencesKey = booleanPreferencesKey(DataStoreConstants.LOGIN_SUCCESSFUL)
            val preferences = context.dataStore.data.first()
            preferences[preferencesKey] ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}