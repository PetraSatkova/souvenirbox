package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SouvenirDetailViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository,
    private val currencyRemoteRepository: ICurrencyRemoteRepository
) : ViewModel(), SouvenirDetailActions {

    private val _uiState: MutableStateFlow<SouvenirDetailUIState> = MutableStateFlow(value = SouvenirDetailUIState())
    val uiState: StateFlow<SouvenirDetailUIState> get() = _uiState

    fun loadSouvenir(id: Long?) {
        if (id == null) {
            return
        }
        viewModelScope.launch {
            val souvenir = souvenirsLocalRepository.getSouvenirById(id)
            _uiState.value = _uiState.value.copy(
                souvenir = souvenir,
                loading = false
            )
        }
    }

    override fun deleteSouvenir() {
        viewModelScope.launch {
            souvenirsLocalRepository.deleteSouvenir(souvenir = _uiState.value.souvenir!!)
            _uiState.value = _uiState.value.copy(
                deleted = true
            )
        }
    }

    override fun convertPrice() {
        viewModelScope.launch {
            val price = currencyRemoteRepository.getRates(
                base = _uiState.value.souvenir?.currency!!,
                symbols = "" // from datastore
            )
//            _uiState.value = _uiState.value.copy(
//                priceInMyCurrency = price
//            )
        }
    }
}