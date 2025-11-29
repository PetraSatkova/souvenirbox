package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

interface AddEditActions {
    fun onNameChanged(text: String)
    fun onLocationChanged()
    fun onPriceChanged(price: Double)
    fun onCurrencyChanged(currency: String)
    fun onDateChanged(date: Long)
    fun onNotesChanged(text: String)
    fun onPhotoChanged(uri: android.net.Uri)
    fun saveSouvenir()

}