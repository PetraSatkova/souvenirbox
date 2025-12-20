package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class SouvenirDetailUIState(
    var loading: Boolean = true,
    val souvenir: SouvenirEntity? = null,
    var deleted: Boolean = false
)