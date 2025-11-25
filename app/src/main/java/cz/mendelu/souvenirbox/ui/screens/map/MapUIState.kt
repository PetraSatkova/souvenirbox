package cz.mendelu.souvenirbox.ui.screens.map

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class MapUIState(
    var loading: Boolean = true,
    val souvenirs: List<SouvenirEntity> = emptyList()
)