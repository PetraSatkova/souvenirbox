package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

data class AddEditUIState(
    var id: Long? = null,
    var name: String? = null,
    var latitude: Double? = null,
    var longitude: Double? = null,
    var city: String? = null,
    var country: String? = null,
    var price: String? = null,
    var currency: String? = null,
    var date: Long? = null,
    var notes: String? = null,
    var imageUri: String? = null,
    var tags: List<String>? = null,

    var loading: Boolean = true,
    var souvenirSaved: Boolean = false,
    var saveError: Boolean = false,

    val currencyList: List<String> = listOf("CZK", "EUR", "USD"),

    var nameError: Boolean = false,
    var cityError: Boolean = false,
    var priceError: Boolean = false,
    var currencyError: Boolean = false,
    var dateError: Boolean = false
)
