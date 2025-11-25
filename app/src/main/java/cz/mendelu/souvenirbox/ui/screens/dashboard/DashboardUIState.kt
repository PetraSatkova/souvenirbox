package cz.mendelu.souvenirbox.ui.screens.dashboard

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class DashboardUIState(
    var loading: Boolean = true,
    val souvenirs: List<SouvenirEntity> = emptyList()
)
