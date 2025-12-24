package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.elements.CustomDatePickerDialog
import cz.mendelu.souvenirbox.ui.elements.InfoElement
import cz.mendelu.souvenirbox.ui.theme.basicMargin
import cz.mendelu.souvenirbox.ui.theme.halfMargin
import cz.mendelu.souvenirbox.ui.theme.quarterMargin
import cz.mendelu.souvenirbox.utils.DateUtils
import kotlinx.coroutines.launch

@Composable
fun AddEditScreen(
    navigation: INavigationRouter,
    viewModel: AddEditViewModel = hiltViewModel<AddEditViewModel>(),
    id: Long?
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = id) {
        viewModel.loadSouvenir(id)
    }

    if (state.value.souvenirSaved) {
        navigation.returnBack()
        viewModel.souvenirSavedDefault()
    }

    BaseScreen(
        topBarText = if (id == null) "Add Souvenir" else "Edit Souvenir",
        onBackClick = {
            navigation.returnBack()
        }
    ) {
        AddEditScreenContent(
            paddingValues = it,
            state = state.value,
            actions = viewModel
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreenContent(
    paddingValues: PaddingValues,
    state: AddEditUIState,
    actions: AddEditActions
) {
    val context = LocalContext.current

    // date
    var showDatePicker by remember {
        mutableStateOf(false)
    }

    // currency dropdown
    var isCurrencyExpanded by remember {
        mutableStateOf(false)
    }

    // gallery
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                // TODO permissions handler?
                actions.onPhotoChanged(uri)

            }
        }
    )

    // bottom sheet
    val sheetVisible = remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    // map bottom sheet remember variables
    val pickedLatLng = remember(state.latitude, state.longitude) {
        mutableStateOf(
            if (state.latitude != null && state.longitude != null)
                LatLng(state.latitude!!, state.longitude!!)
            else null
        )
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(
            pickedLatLng.value ?: LatLng(49.210632, 16.624124),
            if (pickedLatLng.value != null) 12f else 10f
        )
    }

    val markerState = rememberUpdatedMarkerState(
        position = pickedLatLng.value ?: LatLng(49.210632, 16.624124)
    )


    // UI finally
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),

    ) {
        // image
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
                .clickable {
                    galleryLauncher.launch(
                        PickVisualMediaRequest(
                            ActivityResultContracts.PickVisualMedia.ImageOnly
                        )
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (state.imageUri != null) {
                AsyncImage(
                    model = state.imageUri,
                    contentDescription = "img",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.undraw_image_folder),
                    contentDescription = "placeholder"
                )
            }
        }

        Spacer(modifier = Modifier.height(basicMargin()))

        // name
        CustomTextField(
            title = "Name",
            value = state.name ?: "",
            onValueChange = {
                actions.onNameChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            isError = state.nameError
        )

        Spacer(modifier = Modifier.height(halfMargin()))

        // location field
        CustomTextField(
            title = "Location",
            value = if (state.city != null && state.country != null) {
                "${state.city}, ${state.country}"
            } else "",
            onValueChange = { },
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            readonly = true,
            isError = state.cityError,
            trailingIcon = false,
            onClick = { sheetVisible.value = true }
        )


        // map bottom sheet
        if (sheetVisible.value) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                    }.invokeOnCompletion {
                        sheetVisible.value = false
                    }
                },
                sheetState = sheetState,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 460.dp)
                ) {
                    // map
                    GoogleMap(
                        modifier = Modifier.fillMaxSize(),
                        cameraPositionState = cameraPositionState,
                        onMapClick = { latLng ->
                            pickedLatLng.value = latLng
                        }
                    ) {
                        pickedLatLng.value?.let {
                            Marker(
                                state = markerState,
                                title = "Selected location"
                            )
                        }
                    }

                    // select button
                    Button(
                        onClick = {
                            val latLng = pickedLatLng.value ?: return@Button
                            actions.onLocationChanged(
                                latLng.latitude,
                                latLng.longitude
                            )
                            actions.selectPlaceOnMap(
                                context,
                                latLng.latitude,
                                latLng.longitude
                            )

                            scope.launch { sheetState.hide() }
                                .invokeOnCompletion {
                                    sheetVisible.value = false
                                }
                        },
                        enabled = pickedLatLng.value != null,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(basicMargin()),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Select")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(halfMargin()))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(quarterMargin())
        ) {
            // price
            CustomTextField(
                title = "Price",
                value = state.price ?: "",
                onValueChange = { input ->
                    if (input?.matches(Regex("^\\d*\\.?\\d*$")) == true) {
                        actions.onPriceChanged(input)
                    }
                },
                modifier = Modifier
                    .weight(1f),
                isError = state.priceError
            )

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                // currency
                CustomTextField(
                    title = "Currency",
                    value = state.currency ?: "",
                    onValueChange = {},
                    readonly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(halfMargin()),
                    isError = state.currencyError,
                    trailingIcon = false,
                    onClick = {
                        isCurrencyExpanded = true
                    }
                )

                DropdownMenu(
                    expanded = isCurrencyExpanded,
                    onDismissRequest = {
                        isCurrencyExpanded = false
                    }
                ) {
                    // for each z api do ui state?
                    state.currencyList.forEach { currency ->
                        DropdownMenuItem(
                            text = {
                                Text(text = currency)
                            },
                            onClick = {
                                actions.onCurrencyChanged(currency)
                                isCurrencyExpanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(halfMargin()))

        if (showDatePicker) {
            CustomDatePickerDialog(
                date = state.date ,
                onDateSelected = { actions.onDateChanged(it) },
                onDismiss = { showDatePicker = false }
            )
        }

        // date
        InfoElement(
            value = if (state.date != null) DateUtils.getDateString(state.date!!) else null,
            hint = "Date",
            leadingIcon = Icons.Default.DateRange,
            onClick = {
                showDatePicker = true
            },
            onClearClick = {
                actions.onDateChanged(date = null)
            },
            error = state.dateError
        )

        Spacer(modifier = Modifier.height(halfMargin()))

        // notes
        CustomTextField(
            title = "Notes",
            value = state.notes ?: "",
            onValueChange = {
                actions.onNotesChanged(it)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin())
        )

        Spacer(modifier = Modifier.height(basicMargin()))

        // save
        Button(
            onClick = {
                actions.saveSouvenir(
                    context = context,
                    id = state.id
                )
            },
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.app_blue))
        ) {
            Text(text = if (state.id != null) "Update" else "Save")
        }
    }
}

@Composable
fun CustomTextField(
    title: String,
    value: String,
    onValueChange: (String?) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readonly: Boolean = false,
    trailingIcon: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    Box(modifier = modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(text = title) },
            trailingIcon = {
                if (trailingIcon) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        tint = Color.Black,
                        contentDescription = "Clear",
                        modifier = Modifier.clickable {
                            onValueChange(null)
                        }
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = readonly,
            enabled = true,
            isError = isError
        )

        if (onClick != null) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { onClick() }
            )
        }
    }
}

