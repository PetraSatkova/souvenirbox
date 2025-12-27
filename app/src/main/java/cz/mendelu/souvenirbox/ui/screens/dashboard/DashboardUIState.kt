package cz.mendelu.souvenirbox.ui.screens.dashboard

import cz.mendelu.souvenirbox.database.SouvenirEntity

data class DashboardUIState(
    val loading: Boolean = true,
    val recentSouvenirs: List<SouvenirEntity> = emptyList(),
    val favouriteSouvenirs: List<SouvenirEntity> = emptyList(),

)
