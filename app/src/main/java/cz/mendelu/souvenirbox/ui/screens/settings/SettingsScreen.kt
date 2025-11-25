package cz.mendelu.souvenirbox.ui.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {

    BaseScreen(
        topBarText = "Settings",
        showLoading = true
    ) {
        SettingsScreenContent(
            paddingValues = it
        )
    }

}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues
) {
    Text("hej hou")
}