package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class SouvenirDetailUIState(
    var loading: Boolean = true,
    val souvenir: SouvenirEntity? = null,
    var priceInMyCurrency: Double? = null,
    var tags: List<String>? = listOf("tag1", "tag2"),
    var deleted: Boolean = false
)