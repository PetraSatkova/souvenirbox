package cz.mendelu.souvenirbox.ui.screens.souvenirsList

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardViewModel

@Composable
fun SouvenirsListScreen(
    navigation: INavigationRouter,
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "My Souvenirs",
        showLoading = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                }
            ) {
                Icon(Icons.Filled.Add, "add")
            }
        }
    ) {
        SouvenirsListScreenContent(
            paddingValues = it,
        )
    }
}

@Composable
fun SouvenirsListScreenContent(
    paddingValues: PaddingValues
) {
    Text("hola")
}