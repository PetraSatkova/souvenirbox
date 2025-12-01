package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel(), AddEditActions {

    private val _uiState: MutableStateFlow<AddEditUIState> = MutableStateFlow(value = AddEditUIState())
    val uiState: StateFlow<AddEditUIState> get() = _uiState

    fun loadSouvenir(id: Long?) {
        if (id != null) {
            viewModelScope.launch {
                val souvenir = souvenirsLocalRepository.getSouvenirById(id)
            }
        }
    }

    override fun onNameChanged(text: String) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                name = text
            )
        )
    }

    override fun onLocationChanged() {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                latitude = 48.1,
                longitude = 54.8
            )
        )
    }

    override fun onPriceChanged(price: Double) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                price = price
            )
        )
    }

    override fun onCurrencyChanged(currency: String) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                currency = currency
            )
        )
    }

    override fun onDateChanged(date: Long) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                date = date
            )
        )
    }

    override fun onNotesChanged(text: String) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                notes = text
            )
        )
    }

    override fun onPhotoChanged(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            souvenir = _uiState.value.souvenir?.copy(
                imageUri = uri.toString()
            )
        )
    }

    override fun saveSouvenir() {
        viewModelScope.launch {

        }
    }

    fun souvenirSavedDefault() {
        _uiState.value = _uiState.value.copy(
            souvenirSaved = false
        )
    }
}