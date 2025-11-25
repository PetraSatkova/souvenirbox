package cz.mendelu.souvenirbox.ui.screens.map

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    viewModel: MapViewModel = hiltViewModel<MapViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Map",
        showLoading = true
    ) {
        MapScreenContent(
            paddingValues = it,
        )
    }
}

@Composable
fun MapScreenContent(
    paddingValues: PaddingValues
) {

}