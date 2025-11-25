package cz.mendelu.souvenirbox.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<DashboardUIState> = MutableStateFlow(value = DashboardUIState())
    val uiState: StateFlow<DashboardUIState> get() = _uiState

    fun loadSouvenirs() {

    }

}