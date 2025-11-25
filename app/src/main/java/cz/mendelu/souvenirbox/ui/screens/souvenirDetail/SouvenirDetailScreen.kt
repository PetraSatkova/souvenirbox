package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun SouvenirDetailScreen(
    navigation: INavigationRouter,
    viewModel: SouvenirDetailViewModel = hiltViewModel<SouvenirDetailViewModel>()
) {
    BaseScreen(
        topBarText = "Souvenir detail",
        showLoading = true
    ) {
        SouvenirDetailScreenContent(
            paddingValues = it
        )

    }
}

@Composable
fun SouvenirDetailScreenContent(
    paddingValues: PaddingValues
) {
    Text("hello")
}