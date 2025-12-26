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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.theme.basicMargin
import cz.mendelu.souvenirbox.utils.DateUtils
import java.util.Locale
import java.util.Locale.getDefault

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

    if (state.value.deleted) {
        navigation.returnBack()
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
            state = state.value,
            actions = viewModel
        )

    }
}

@Composable
fun SouvenirDetailScreenContent(
    paddingValues: PaddingValues,
    state: SouvenirDetailUIState,
    actions: SouvenirDetailActions
) {
    val iso2 = state.souvenir?.countryCode?.trim()?.lowercase(Locale.ROOT)
    val flagUrl = iso2?.let { "https://flagcdn.com/48x36/$it.png" }

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
                        .clip(shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!state.souvenir?.imageUri.isNullOrEmpty()) {
                        AsyncImage(
                            model = state.souvenir.imageUri,
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(R.drawable.undraw_image_folder),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                // flag
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(shape = CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (flagUrl == null) {
                        Text("🏳️")
                    } else {
                        AsyncImage(
                            model = flagUrl,
                            contentDescription = null,
                            modifier = Modifier.size(54.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
        // head
        item {
            Text(
                text = state.souvenir?.name?.uppercase(getDefault()) ?: "NAME",
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
            }
        }

        // chips of tags
        item {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(horizontal = basicMargin())
            ) {
                items(
                    count = state.souvenir?.tags?.size ?: 2,
                ) { index ->
                    SmallAssistChip(text = state.souvenir?.tags?.get(index) ?: "tag")
                }
            }
        }

        // location card
        item {
            InfoCard(
                title = "Location",
                content = {
                    LabeledValue(label = "City", value = state.souvenir?.city ?: "city")
                    Spacer(Modifier.height(8.dp))
                    LabeledValue(label = "Country", value = state.souvenir?.country ?: "country")
                }
            )
        }

        // price card
        item {
            InfoCard(
                title = "Price",
                content = {
                    PriceRow(
                        label = "Local (${state.souvenir?.currency})",
                        value = state.souvenir?.price.toString()
                    )
                    Spacer(Modifier.height(10.dp))
                    PriceRow(
                        label = "My currency (${state.souvenir?.currency})", // TODO from datastore
                        value = "My price form api" // TODO from API
                    )
                }
            )
        }

        // notes card
        item {
            InfoCard(
                title = "Notes",
                content = {
                    Text(
                        text = state.souvenir?.notes ?: "No thoughts about this one",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            )
        }

        item {

        }

        // delete button
        item {
            Button(
                onClick = {
                    actions.deleteSouvenir()
                },
                modifier = Modifier
                    .padding(basicMargin())
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.delete))
            ) {
                Text("Delete")
            }
        }

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
                .padding(basicMargin()),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
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
