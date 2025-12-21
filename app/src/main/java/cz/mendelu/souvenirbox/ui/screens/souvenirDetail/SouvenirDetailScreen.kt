package cz.mendelu.souvenirbox.ui.screens.souvenirDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.theme.basicMargin
import cz.mendelu.souvenirbox.utils.DateUtils

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
            state = state.value
        )

    }
}

@Composable
fun SouvenirDetailScreenContent(
    paddingValues: PaddingValues,
    state: SouvenirDetailUIState
) {
    LazyColumn(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        item {
            Row(
                modifier = Modifier
                    .padding(basicMargin())
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // photo
                Box(
                    modifier = Modifier
                        .size(140.dp)
                        .clip(shape = RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (!state.souvenir?.imageUri.isNullOrEmpty()) {
                        AsyncImage(
                            model = state.souvenir.imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.undraw_image_folder),
                            contentDescription = null,
                            contentScale = ContentScale.Fit
                        )
                    }
                }
                // flag
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    // TODO load flag from api
                }
            }
        }

        item {
            Text(
                text = state.souvenir?.name ?: "name",
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(horizontal = basicMargin())
            )

            Spacer(Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = basicMargin())
            ) {
                Text(
                    text = if (state.souvenir?.date != null) DateUtils.getDateString(state.souvenir.date) else "date",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "*",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.souvenir?.city ?: "city",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "*",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = state.souvenir?.country ?: "country",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // chips of tags
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = basicMargin())
            ) {
                items(
                    count = state.tags?.size ?: 0,
                ) { index ->
                    SmallAssistChip(text = state.tags?.get(index) ?: "tag")
                }
            }
        }

        // cards
        item {
            InfoCard(
                title = "Location",
                subtitle = "Where you got it",
                content = {
                    LabeledValue(label = "City", value = state.souvenir?.city ?: "city")
                    Spacer(Modifier.height(8.dp))
                    LabeledValue(label = "Country", value = state.souvenir?.country ?: "country")
                },
                trailing = {
                    Icon(
                        imageVector = Icons.Default.Place,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        // price card
        item {
            InfoCard(
                title = "Price",
                subtitle = "Local and your currency",
                content = {
                    PriceRow(
                        label = "Local (${state.souvenir?.currency})",
                        value = state.souvenir?.price.toString()
                    )
                    Spacer(Modifier.height(10.dp))
                    PriceRow(
                        label = "My currency from datastore", // TODO from datastore
                        value = "My price form api" // TODO from API
                    )
                }
            )
        }

        // notes card
        item {
            InfoCard(
                title = "Notes",
                subtitle = "My thoughts",
                content = {
                    Text(
                        text = state.souvenir?.notes ?: "No thoughts about this one",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        // delete button

    }
}


@Composable
private fun SmallAssistChip(
    text: String
) {
    AssistChip(
        onClick = {},
        label = { Text(text) },
        shape = RoundedCornerShape(12.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = colorResource(R.color.app_tyrkys)
        ),
        border = null
    )
}

@Composable
private fun InfoCard(
    title: String,
    subtitle: String,
    content: @Composable ColumnScope.() -> Unit,
    trailing: @Composable (() -> Unit)? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                if (trailing != null) {
                    Spacer(Modifier.width(10.dp))
                    trailing()
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Column(content = content)
        }
    }
}

@Composable
private fun LabeledValue(label: String, value: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PriceRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
    }
}
