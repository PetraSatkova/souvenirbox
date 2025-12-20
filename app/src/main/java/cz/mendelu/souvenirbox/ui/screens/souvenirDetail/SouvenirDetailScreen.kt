package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.theme.basicMargin

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
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(basicMargin())
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // image
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (!souvenir.imageUri.isNullOrEmpty()) {
                        Image(
                            painter = rememberAsyncImagePainter(model = souvenir.imageUri),
                            contentDescription = "Selected image",
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.undraw_image_folder),
                            contentDescription = "place photo",
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                // flag
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO load flag from api
                }
            }
        }
    }
}