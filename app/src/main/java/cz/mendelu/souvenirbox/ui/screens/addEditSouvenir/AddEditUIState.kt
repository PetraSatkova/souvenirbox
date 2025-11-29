package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class AddEditUIState(
    var loading: Boolean = true,
    val souvenir: SouvenirEntity? = null,
    var souvenirSaved: Boolean = false,
    val currencyList: List<String> = listOf("CZK", "EUR", "USD")
)
