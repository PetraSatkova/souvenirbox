package cz.mendelu.souvenirbox.ui.screens.map

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class MapUIState(
    val loading: Boolean = true,
    val souvenirs: List<SouvenirEntity> = emptyList()
)