package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun SouvenirDetailScreen(
    navigation: INavigationRouter,
    viewModel: SouvenirDetailViewModel = hiltViewModel<SouvenirDetailViewModel>(),
    id: Long
) {
    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        viewModel.loadSouvenir(id)
    }

    BaseScreen(
        topBarText = "Souvenir detail",
        showLoading = state.value.loading,
        onBackClick = {
            navigation.returnBack()
        },
        actions = {
            IconButton(onClick = {
                navigation.navigateToAddEdit(id)
            }
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "edit"
                )
            }
        }
    ) {
        SouvenirDetailScreenContent(
            paddingValues = it,
            souvenir = state.value.souvenir!!
        )

    }
}

@Composable
fun SouvenirDetailScreenContent(
    paddingValues: PaddingValues,
    souvenir: SouvenirEntity
) {
    Text("hello")
}