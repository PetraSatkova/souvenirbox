package cz.mendelu.souvenirbox.ui.screens.map

import androidx.lifecycle.ViewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
): ViewModel() {

    private val _uiState: MutableStateFlow<MapUIState> = MutableStateFlow(value = MapUIState())
    val uiState: StateFlow<MapUIState> get() = _uiState


}