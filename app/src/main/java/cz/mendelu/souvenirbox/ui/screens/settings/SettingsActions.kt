package cz.mendelu.souvenirbox.ui.screens.settings

interface SettingsActions {
    fun setTheme(enabled: Boolean)
    fun setCurrency(currency: String)
    fun setLanguage(language: String)
}