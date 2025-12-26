package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cz.mendelu.souvenirbox.analyzers.ImageTagger
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.database.SouvenirEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject
import kotlin.collections.emptyList
import kotlin.collections.firstOrNull
import androidx.core.net.toUri

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel(), AddEditActions {

    private val _uiState: MutableStateFlow<AddEditUIState> =
        MutableStateFlow(value = AddEditUIState())
    val uiState: StateFlow<AddEditUIState> get() = _uiState

    private val imageTagger: ImageTagger = ImageTagger()
    
    fun loadSouvenir(id: Long?) {
        if (id != null) {
            viewModelScope.launch {
                val souvenir = souvenirsLocalRepository.getSouvenirById(id)
                _uiState.value = _uiState.value.copy(
                    id = id,
                    name = souvenir.name,
                    latitude = souvenir.latitude,
                    longitude = souvenir.longitude,
                    city = souvenir.city,
                    country = souvenir.country,
                    countryCode = souvenir.countryCode,
                    price = souvenir.price.toString(),
                    currency = souvenir.currency,
                    date = souvenir.date,
                    notes = souvenir.notes,
                    imageUri = souvenir.imageUri,
                    tags = souvenir.tags
                )
            }
        }
    }

    override fun onNameChanged(text: String?) {
        _uiState.value = _uiState.value.copy(
            name = text,
            nameError = false
        )
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
        _uiState.value = _uiState.value.copy(
            latitude = latitude,
            longitude = longitude,
            cityError = false
        )
    }

    override fun onPriceChanged(price: String?) {
        _uiState.value = _uiState.value.copy(
            price = price,
            priceError = false
        )
    }

    override fun onCurrencyChanged(currency: String) {
        _uiState.value = _uiState.value.copy(
            currency = currency,
            currencyError = false
        )
    }

    override fun onDateChanged(date: Long?) {
        _uiState.value = _uiState.value.copy(
            date = date,
            dateError = false
        )
    }

    override fun onNotesChanged(text: String?) {
        _uiState.value = _uiState.value.copy(
            notes = text
        )
    }

    override fun onPhotoChanged(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            imageUri = uri.toString()
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun saveSouvenir(
        context: Context,
        id: Long?
    ) {
        if (isInputValid()) {
            return
        }

        viewModelScope.launch {
            createTags(context)

            val newSouvenir = SouvenirEntity(
                name = _uiState.value.name ?: "No name",
                latitude = _uiState.value.latitude ?: 0.0,
                longitude = _uiState.value.longitude ?: 0.0,
                city = _uiState.value.city ?: "Na vi",
                country = _uiState.value.country ?: "Pandora",
                countryCode = _uiState.value.countryCode ?: "",
                price = _uiState.value.price?.toDouble() ?: 0.0,
                currency = _uiState.value.currency ?: "EUR",
                date = _uiState.value.date ?: System.currentTimeMillis(),
                isFavourite = false,
                notes = _uiState.value.notes ?: "",
                imageUri = _uiState.value.imageUri ?: "",
                tags = _uiState.value.tags
            )

            if (id == null) {
                souvenirsLocalRepository.createSouvenir(newSouvenir)
            } else {
                souvenirsLocalRepository.updateSouvenir(newSouvenir)
            }
            _uiState.value = _uiState.value.copy(
                saveError = false,
                nameError = false,
                cityError = false,
                priceError = false,
                currencyError = false,
                dateError = false,
                souvenirSaved = true
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun selectPlaceOnMap(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
        viewModelScope.launch {
            getCityFromLatLng(
                context = context,
                latitude = latitude,
                longitude = longitude
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    suspend fun getCityFromLatLng(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String? = suspendCancellableCoroutine { continuation ->

        try {
            val geocoder = Geocoder(context, Locale.getDefault())

            geocoder.getFromLocation(latitude, longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(results: MutableList<Address>) {
                    if (!continuation.isActive) return

                    val address = results.firstOrNull()

                    // Try to get something that looks like a city
                    val city = address?.locality
                        ?: address?.subAdminArea   // e.g. district/county
                        ?: address?.adminArea      // e.g. region/state
                        ?: address?.featureName    // last fallback

                    _uiState.value = _uiState.value.copy(
                        city = city,
                        country = address?.countryName ?: address?.countryCode,
                        countryCode = address?.countryCode
                    )

                    continuation.resume(city) { cause, _, _ -> }
                }

                override fun onError(errorMessage: String?) {
                    if (!continuation.isActive) return
                    continuation.resume(null) { cause, _, _ -> }
                }
            })
        } catch (e: IOException) {
            if (continuation.isActive) {
                continuation.resume(null) { cause, _, _ -> }
            }
        }

        continuation.invokeOnCancellation { }
    }

    fun isInputValid(): Boolean {
        var error = false

        if (_uiState.value.name.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(
                nameError = true
            )
            error = true
        }

        if (_uiState.value.latitude == null ||
            _uiState.value.longitude == null ||
            _uiState.value.city.isNullOrEmpty() ||
            _uiState.value.country.isNullOrEmpty()
            ) {
            _uiState.value = _uiState.value.copy(
                cityError = true
            )
            error = true
        }
        
        if (_uiState.value.price.isNullOrEmpty()) {
            _uiState.value = _uiState.value.copy(
                priceError = true
            )
            error = true
        }
        if (_uiState.value.currency == null) {
            _uiState.value = _uiState.value.copy(
                currencyError = true
            )
            error = true
        }
        if (_uiState.value.date == null) {
            _uiState.value = _uiState.value.copy(
                dateError = true
            )
            error = true
        }
        return error
    }

    fun souvenirSavedDefault() {
        _uiState.value = _uiState.value.copy(
            souvenirSaved = false
        )
    }

    private suspend fun createTags(
        context: Context
    ) {
        if (_uiState.value.imageUri == null) return

        val tags = try {
            withContext(Dispatchers.IO) {
                imageTagger.generateTags(
                    context = context,
                    imageUri = _uiState.value.imageUri?.toUri()!!
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
        _uiState.value = _uiState.value.copy(
            tags = tags
        )
    }
}
