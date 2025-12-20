package cz.mendelu.souvenirbox.ui.screens.addEditSouvenir

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.ui.elements.CustomDatePickerDialog
import cz.mendelu.souvenirbox.ui.elements.InfoElement
import cz.mendelu.souvenirbox.ui.theme.appBlue
import cz.mendelu.souvenirbox.ui.theme.basicMargin
import cz.mendelu.souvenirbox.ui.theme.halfMargin
import cz.mendelu.souvenirbox.ui.theme.quarterMargin
import cz.mendelu.souvenirbox.utils.DateUtils

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

@Composable
fun AddEditScreenContent(
    paddingValues: PaddingValues,
    state: AddEditUIState,
    actions: AddEditActions
) {
    val context = LocalContext.current

    var showDatePicker by remember {
        mutableStateOf(false)
    }
    var isCurrencyExpanded by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize(),

    ) {
        // image TODO clickable
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (state.imageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = state.imageUri),
                    contentDescription = "img",
                    modifier = Modifier
                        .size(100.dp)
                )
            } else {
                Image(
                    painter = painterResource(R.drawable.undraw_image_folder),
                    contentDescription = "placeholder"
                )
            }
        }

        Spacer(modifier = Modifier.height(basicMargin()))

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

        // todo show map - nebude mat onvaluechange, ale bude clickable
        CustomTextField(
            title = "Location",
            value = "Oslo",
            onValueChange = {
                actions.onLocationChanged()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            readonly = true,
            isError = state.cityError
        )

        Spacer(modifier = Modifier.height(halfMargin()))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(halfMargin()),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(quarterMargin())
        ) {
            CustomTextField(
                title = "Price",
                value = state.price?.toString() ?: "",
                onValueChange = {
                    actions.onPriceChanged(it.toDoubleOrNull())
                },
                modifier = Modifier
                    .weight(1f),
                isError = state.priceError
            )

            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                CustomTextField(
                    title = "Currency",
                    value = state.currency ?: "",
                    onValueChange = {},
                    readonly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
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
//            colors = ButtonColors()
        ) {
            Text(text = if (state.id != null) "Update" else "Save")
        }
    }
}

@Composable
fun CustomTextField(
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    readonly: Boolean = false,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.Cancel,
                tint = Color.Black,
                contentDescription = "Clear"
            )
        },
        modifier = modifier,
        readOnly = readonly,
        isError = isError
    )
}
