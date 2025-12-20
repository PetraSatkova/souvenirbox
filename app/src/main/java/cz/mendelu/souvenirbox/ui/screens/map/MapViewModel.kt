package cz.mendelu.souvenirbox.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<MapUIState> = MutableStateFlow(value = MapUIState())
    val uiState: StateFlow<MapUIState> get() = _uiState

    init {
        viewModelScope.launch {
            val souvenirs = souvenirsLocalRepository.getAllSouvenirs()
            _uiState.value = _uiState.value.copy(
                souvenirs = souvenirs,
                loading = false
            )
        }
    }
}