package cz.mendelu.souvenirbox.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.datastore.IDataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository,
    private val dataStore: IDataStoreRepository
) : ViewModel()
{
    private val _uiState: MutableStateFlow<SettingsUIState> = MutableStateFlow(value = SettingsUIState())

    val uiState: StateFlow<SettingsUIState> =
        dataStore.darkThemeFlow
            .map { SettingsUIState(darkTheme = it) }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                SettingsUIState()
            )

    fun setTheme(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.setDarkTheme(enabled)
            _uiState.value = _uiState.value.copy(darkTheme = enabled)
        }
    }

}