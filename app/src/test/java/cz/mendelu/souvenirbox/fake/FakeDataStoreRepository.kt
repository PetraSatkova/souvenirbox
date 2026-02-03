package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.datastore.IDataStoreRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeDataStoreRepository : IDataStoreRepository {

    private val _darkThemeFlow = MutableStateFlow(false)
    override val darkThemeFlow: StateFlow<Boolean> = _darkThemeFlow

    private val _currencyFlow = MutableStateFlow("EUR")
    override val currencyFlow: StateFlow<String> = _currencyFlow

    private val _languageFlow = MutableStateFlow("en")
    override val languageFlow: StateFlow<String> = _languageFlow

    override suspend fun setDarkTheme(value: Boolean) {
        _darkThemeFlow.value = value
    }

    override suspend fun setCurrency(value: String) {
        _currencyFlow.value = value
    }

    override suspend fun getCurrency(): String {
        return _currencyFlow.value
    }

    override suspend fun setLanguage(value: String) {
        _languageFlow.value = value
    }

    override suspend fun getLanguage(): String {
        return _languageFlow.value
    }
}
