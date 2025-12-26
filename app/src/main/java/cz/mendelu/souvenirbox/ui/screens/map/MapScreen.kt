package cz.mendelu.souvenirbox.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

@Composable
fun MapScreen(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: MapViewModel = hiltViewModel<MapViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Map",
        showLoading = state.value.loading
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
        position = CameraPosition.fromLatLngZoom(
            // TODO set position dynamically
            LatLng(49.210632, 16.624124),
            10.0f
        )
    }

    // TODO bottomsheet?
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

// TODO generated
@Composable
fun SouvenirSheetContent(
    souvenir: SouvenirEntity
) {

}
