package cz.mendelu.souvenirbox.ui.screens.souvenirsList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SouvenirsListViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SouvenirsListUIState> = MutableStateFlow(value = SouvenirsListUIState())
    val uiState: StateFlow<SouvenirsListUIState> get() = _uiState

    fun loadSouvenirs() {
        viewModelScope.launch {
            val souvenirs = souvenirsLocalRepository.getAllSouvenirs()
            _uiState.value = _uiState.value.copy(
                souvenirs = souvenirs.sortedByDescending { it.date },
                loading = false
            )

        }
    }
}