package cz.mendelu.souvenirbox.ui.screens.souvenirsList

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class SouvenirsListUIState(
    val loading: Boolean = true,
    val souvenirs: List<SouvenirEntity>? = null
)
