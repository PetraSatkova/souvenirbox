package cz.mendelu.souvenirbox.ui.screens.dashboard

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun DashboardScreen(
    navigation: INavigationRouter,
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Dashboard",
        showLoading = true
    ) {
        DashboardScreenContent(
            paddingValues = it,
        )
    }
}

@Composable
fun DashboardScreenContent(
    paddingValues: PaddingValues
) {
    Text("hola")
}