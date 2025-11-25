package cz.mendelu.souvenirbox.ui.activities

sealed class SplashScreenUiState {
    object Default : SplashScreenUiState()
    object ShowLogin : SplashScreenUiState()
    object ContinueToApp : SplashScreenUiState()
}