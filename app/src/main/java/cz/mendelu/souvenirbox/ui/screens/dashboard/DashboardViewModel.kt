package cz.mendelu.souvenirbox.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DashboardUIState> = MutableStateFlow(value = DashboardUIState())
    val uiState: StateFlow<DashboardUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            souvenirsLocalRepository.getAllSouvenirs().collect { list ->
                _uiState.value = _uiState.value.copy(
                    recentSouvenirs = list.sortedByDescending { it.date },
                    favouriteSouvenirs = list.filter { it.isFavourite },
                    loading = false
                )
            }
        }
    }
}