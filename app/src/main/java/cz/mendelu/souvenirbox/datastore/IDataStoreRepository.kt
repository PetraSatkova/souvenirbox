package cz.mendelu.souvenirbox.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    val darkThemeFlow: Flow<Boolean>
    suspend fun setLoginSuccessful()
    suspend fun getLoginSuccessful(): Boolean
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setLanguage(value: String)
    suspend fun getLanguage(): String
    suspend fun setCurrency(value: String)
    suspend fun getCurrency(): String
}
