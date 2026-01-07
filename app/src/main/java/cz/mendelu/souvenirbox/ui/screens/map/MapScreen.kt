package cz.mendelu.souvenirbox.ui.screens.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.AssistChip
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.ClusterRenderer
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.rememberCameraPositionState
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.map.ClusterMapRenderer
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: MapViewModel = hiltViewModel<MapViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    val isFilterExpanded = remember { mutableStateOf(false) }

    BaseScreen(
        topBarText = "Map",
        showLoading = state.value.loading,
        actions = {
            IconButton(onClick = {

            }
            ) {
                Icon(
                    imageVector = Icons.Default.FilterAlt,
                    contentDescription = "filter"
                )
            }
        }
    ) {
        MapScreenContent(
            paddingValuesBottom = paddingValues,
            paddingValuesTop = it,
            state = state.value,
            navigation = navigation
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, MapsComposeExperimentalApi::class)
@Composable
fun MapScreenContent(
    paddingValuesBottom: PaddingValues,
    paddingValuesTop: PaddingValues,
    state: MapUIState,
    navigation: INavigationRouter
) {
    val context = LocalContext.current

    val cameraPosition = rememberCameraPositionState {
        val lat = state.souvenirs.firstOrNull()?.latitude ?: 49.195999
        val lon = state.souvenirs.firstOrNull()?.longitude ?: 16.608419

        position = CameraPosition.fromLatLngZoom(
            LatLng(lat, lon),
            10.0f
        )
    }

    // bottom sheet
    var selected by remember { mutableStateOf<SouvenirEntity?>(null) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // managers
    val clusterManager = remember { mutableStateOf<ClusterManager<SouvenirEntity>?>(null) }
    val rendererRef = remember { mutableStateOf<ClusterRenderer<SouvenirEntity>?>(null) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValuesTop.calculateTopPadding(),
                bottom = paddingValuesBottom.calculateBottomPadding()
            )
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPosition
        ) {
            MapEffect(Unit) { map ->
                val cm = ClusterManager<SouvenirEntity>(context, map)
                val renderer = ClusterMapRenderer(context, map, cm)
                cm.renderer = renderer

                map.setOnCameraIdleListener(cm)
                map.setOnMarkerClickListener(cm)

                cm.setOnClusterItemClickListener { item ->
                    selected = item
                    scope.launch { sheetState.show() }
                    true
                }

                clusterManager.value = cm
                rendererRef.value = renderer
            }

            // souvenirs
            MapEffect(state.souvenirs) {
                val cm = clusterManager.value ?: return@MapEffect
                cm.clearItems()
                cm.addItems(state.souvenirs)
                cm.cluster()
            }
        }
    }

    if (selected != null) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch { sheetState.hide() }.invokeOnCompletion { selected = null }
            },
            sheetState = sheetState,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            SouvenirSheetContent(souvenir = selected!!)
        }
    }
}

@Composable
fun SouvenirSheetContent(
    souvenir: SouvenirEntity,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
            .padding(bottom = 16.dp)
    ) {

        Column(Modifier.fillMaxWidth()) {

            // Image (optional)
            if (!souvenir.imageUri.isNullOrBlank()) {
                AsyncImage(
                    model = souvenir.imageUri,
                    contentDescription = souvenir.name,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                )
            } else {
                // Placeholder
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Photo,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Column(Modifier.padding(16.dp)) {
                // Title row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(
                            text = souvenir.name.uppercase(),
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Spacer(Modifier.height(4.dp))

                        val location = buildString {
                            append(souvenir.city)
                            if (souvenir.country.isNotBlank()) {
                                append(", ")
                                append(souvenir.country)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Place,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = location,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Price + date row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AssistChip(
                        onClick = { },
                        label = {
                            Text("${formatPrice(souvenir.price)} ${souvenir.currency}")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )

                    AssistChip(
                        onClick = { },
                        label = { Text(formatDate(souvenir.date)) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Event,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )
                }

                // Tags
                val tags = souvenir.tags.orEmpty().filter { it.isNotBlank() }
                if (tags.isNotEmpty()) {
                    Spacer(Modifier.height(12.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        tags.take(12).forEach { tag ->
                            SuggestionChip(
                                onClick = { },
                                label = { Text("#$tag") }
                            )
                        }
                        if (tags.size > 12) {
                            AssistChip(
                                onClick = { },
                                label = { Text("+${tags.size - 12}") }
                            )
                        }
                    }
                }

                // Notes preview
                if (souvenir.notes.isNotBlank()) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = souvenir.notes,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

private fun formatDate(epochMillis: Long): String {
    val sdf = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return sdf.format(Date(epochMillis))
}

private fun formatPrice(value: Double): String {
    // Simple formatting (avoid locale surprises); tweak as you like
    return if (kotlin.math.abs(value % 1.0) < 0.000001) value.toLong().toString()
    else String.format(Locale.US, "%.2f", value)
}
