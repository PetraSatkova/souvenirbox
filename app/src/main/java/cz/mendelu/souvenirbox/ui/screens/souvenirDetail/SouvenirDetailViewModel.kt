package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.datastore.IDataStoreRepository
import cz.mendelu.souvenirbox.dispatcher.AppDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class SouvenirDetailViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository,
    private val currencyRemoteRepository: ICurrencyRemoteRepository,
    private val dataStore: IDataStoreRepository,
    private val dispatchers: AppDispatchers
) : ViewModel(), SouvenirDetailActions {

    private val _uiState: MutableStateFlow<SouvenirDetailUIState> = MutableStateFlow(value = SouvenirDetailUIState())
    val uiState: StateFlow<SouvenirDetailUIState> get() = _uiState

    fun loadSouvenir(id: Long?) {
        if (id == null) {
            return
        }
        viewModelScope.launch {
            val souvenir = souvenirsLocalRepository.getSouvenirById(id)
            val myCurrency = dataStore.currencyFlow.first()

            _uiState.value = _uiState.value.copy(
                souvenir = souvenir,
                myCurrency = myCurrency,
                loading = false,
                isFavourite = souvenir.isFavourite
            )

            convertPrice(
                souvenir = souvenir,
                myCurrency = myCurrency
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

    private suspend fun convertPrice(souvenir: SouvenirEntity?, myCurrency: String?) {

        if (souvenir == null || myCurrency == null) {
            return
        }

        if (myCurrency == souvenir.currency) {
            _uiState.value = _uiState.value.copy(priceInMyCurrency = souvenir.price)
            return
        }

        val currencyRate = withContext(dispatchers.io) {
            currencyRemoteRepository.getRates(
                base = souvenir.currency,
                symbols = myCurrency
            )
        }

        when(currencyRate) {
            is CommunicationResult.Success -> {
                val rates = currencyRate.data.rates[myCurrency] ?: currencyRate.data.rates.firstNotNullOf { it.value }

                _uiState.value = _uiState.value.copy(
                    priceInMyCurrency = BigDecimal(souvenir.price * rates)
                        .setScale(2, RoundingMode.HALF_UP)
                        .toDouble(),
                    error = null
                )
            }
            is CommunicationResult.ConnectionError -> {
                _uiState.value = _uiState.value.copy(
                    error = R.string.no_internet_connection
                )
            }

            is CommunicationResult.Error -> {
                _uiState.value = _uiState.value.copy(
                    error = R.string.failed_to_load_currencies
                )
            }

            is CommunicationResult.Exception -> {
                _uiState.value = _uiState.value.copy(
                    error = R.string.exception
                )
            }
        }
    }

    fun updateFavourite(id: Long) {
        viewModelScope.launch {
            souvenirsLocalRepository.updateFavourite(id = id)
            _uiState.value = _uiState.value.copy(
                isFavourite = !_uiState.value.isFavourite
            )
        }
    }
}