package cz.mendelu.souvenirbox.fake

import android.content.Context
import android.net.Uri
import cz.mendelu.souvenirbox.ui.screens.addEditSouvenir.AddEditActions

class FakeAddEditActions: AddEditActions {
    override fun onNameChanged(text: String?) {
    }

    override fun onLocationChanged(latitude: Double, longitude: Double) {
    }

    override fun onPriceChanged(price: String?) {
    }

    override fun onCurrencyChanged(currency: String) {
    }

    override fun onDateChanged(date: Long?) {
    }

    override fun onNotesChanged(text: String?) {
    }

    override fun onPhotoChanged(uri: Uri) {
    }

    override fun saveSouvenir(context: Context, id: Long?) {
    }

    override fun selectPlaceOnMap(
        context: Context,
        latitude: Double,
        longitude: Double
    ) {
    }
}