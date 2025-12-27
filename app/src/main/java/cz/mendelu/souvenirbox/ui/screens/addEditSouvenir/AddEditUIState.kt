package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

data class AddEditUIState(
    val id: Long? = null,
    val name: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val city: String? = null,
    val country: String? = null,
    val countryCode: String? = null,
    val price: String? = null,
    val currency: String? = null,
    val date: Long? = null,
    val notes: String? = null,
    val imageUri: String? = null,
    val tags: List<String>? = null,

    val loading: Boolean = true,
    val souvenirSaved: Boolean = false,
    val saveError: Boolean = false,

    val currencyList: List<String> = emptyList(),

    val nameError: Boolean = false,
    val cityError: Boolean = false,
    val priceError: Boolean = false,
    val currencyError: Boolean = false,
    val dateError: Boolean = false,
    val error: Int? = null
)
