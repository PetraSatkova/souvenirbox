package cz.mendelu.souvenirbox.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val currencyApi: ICurrencyRemoteRepository,
    private val dataStore: IDataStoreRepository
) : ViewModel(), SettingsActions
{
    private val _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(value = SettingsUIState(
        darkTheme = dataStore.darkThemeFlow.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    ))
    val uiState: StateFlow<SettingsUIState> get() = _uiState

    init {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                currencyApi.getCurrencies()
            }

            when(result) {
                is CommunicationResult.Success -> {
                    _uiState.value = _uiState.value.copy(
                        currencyOptions = result.data.keys.toList()
                    )
                }
                is CommunicationResult.ConnectionError -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.no_internet_connection
                    )
                }
                is CommunicationResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.failed_to_load_currencies
                    )
                }
                is CommunicationResult.Exception -> {
                    _uiState.value = _uiState.value.copy(
                        error = R.string.exception
                    )
                }
            }
        }
    }

    override fun setTheme(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkTheme(enabled)
        }
    }

    override fun setCurrency(currency: String) {
        viewModelScope.launch {
            dataStore.setCurrency(value = currency)
            _uiState.value = _uiState.value.copy(currency = currency)

        }
    }

    override fun setLanguage(language: String) {
        viewModelScope.launch {
            dataStore.setCurrency(value = language)
            _uiState.value = _uiState.value.copy(currency = language)

        }
    }

    override fun onQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
    }



}