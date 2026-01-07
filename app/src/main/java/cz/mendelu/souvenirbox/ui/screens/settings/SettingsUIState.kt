package cz.mendelu.souvenirbox.ui.screens.settings

import kotlinx.coroutines.flow.StateFlow

data class SettingsUIState(
    val darkTheme: Boolean = false,
    val currency: String = "EUR",
    val language: String = "en",
    val currencyOptions: List<String> = emptyList(),
    val query: String = "",
    val error: Int? = null
)