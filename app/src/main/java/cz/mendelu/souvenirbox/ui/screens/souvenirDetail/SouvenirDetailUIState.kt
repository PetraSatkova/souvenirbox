package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class SouvenirDetailUIState(
    val loading: Boolean = true,
    val souvenir: SouvenirEntity? = null,
    val myCurrency: String? = null,
    val priceInMyCurrency: Double? = null,
    val deleted: Boolean = false,
    val error: Int? = null,
    val isFavourite: Boolean = false
)