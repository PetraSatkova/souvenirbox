package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.lifecycle.ViewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class SouvenirDetailViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<SouvenirDetailUIState> = MutableStateFlow(value = SouvenirDetailUIState())
    val uiState: StateFlow<SouvenirDetailUIState> get() = _uiState


}