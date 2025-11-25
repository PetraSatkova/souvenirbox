package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import androidx.lifecycle.ViewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<AddEditUIState> = MutableStateFlow(value = AddEditUIState())
    val uiState: StateFlow<AddEditUIState> get() = _uiState

    fun saveSouvenir() {

    }
}