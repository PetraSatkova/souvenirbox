package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import android.content.Context
import kotlinx.coroutines.suspendCancellableCoroutine

interface AddEditActions {
    fun onNameChanged(text: String)
    fun onLocationChanged(latitude: Double, longitude: Double)
    fun onPriceChanged(price: Double?)
    fun onCurrencyChanged(currency: String)
    fun onDateChanged(date: Long?)
    fun onNotesChanged(text: String)
    fun onPhotoChanged(uri: android.net.Uri)
    fun saveSouvenir(
        context: Context,
        id: Long?
    )
    fun selectPlaceOnMap(
        context: Context,
        latitude: Double,
        longitude: Double
    )
}