package cz.mendelu.souvenirbox.datastore

import kotlinx.coroutines.flow.Flow

interface IDataStoreRepository {
    val darkThemeFlow: Flow<Boolean>
    val currencyFlow: Flow<String>
    val languageFlow: Flow<String>
    suspend fun setDarkTheme(value: Boolean)
    suspend fun setCurrency(value: String)
    suspend fun getCurrency(): String
    suspend fun setLanguage(value: String)
    suspend fun getLanguage(): String

}
