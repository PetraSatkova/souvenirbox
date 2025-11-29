package cz.mendelu.souvenirbox.ui.screens.souvenirsList

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.elements.PlaceholderScreenContent
import cz.mendelu.souvenirbox.ui.theme.basicMargin
import cz.mendelu.souvenirbox.ui.theme.quarterMargin
import cz.mendelu.souvenirbox.utils.DateUtils

@Composable
fun SouvenirsListScreen(
    navigation: INavigationRouter,
    viewModel: SouvenirsListViewModel = hiltViewModel<SouvenirsListViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = "list") {
        viewModel.loadSouvenirs()
    }

    BaseScreen(
        topBarText = "My Souvenirs",
        showLoading = true,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigation.navigateToAddEdit(id = null)
                }
            ) {
                Icon(Icons.Filled.Add, "add")
            }
        },
        placeholderScreenContent =
            if (state.value.souvenirs.isEmpty()) {
                PlaceholderScreenContent(
                    title = "No places registered yet!",
                    image = R.drawable.undraw_no_data
                )
            } else {
                null
            }

    ) {
        SouvenirsListScreenContent(
            paddingValues = it,
            navigation = navigation,
            souvenirs = state.value.souvenirs

        )
    }
}

@Composable
fun SouvenirsListScreenContent(
    paddingValues: PaddingValues,
    navigation: INavigationRouter,
    souvenirs: List<SouvenirEntity>
) {
    LazyColumn(
        modifier = Modifier.padding(paddingValues)
    ) {
        souvenirs.forEach { souvenir ->
            item { // todo my colors
                Card(
                    onClick = {
                        navigation.navigateToSouvenirDetail(souvenir.id!!)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = basicMargin(),
                            vertical = quarterMargin()
                        ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(contentColor = MaterialTheme.colorScheme.surface)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(basicMargin())
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(shape = CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            // image
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

                        Spacer(modifier = Modifier.width(basicMargin()))

                        Column {
                            Text(
                                text = souvenir.name,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(quarterMargin()))

                            Text(
                                text = DateUtils.getDateString(souvenir.date),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}