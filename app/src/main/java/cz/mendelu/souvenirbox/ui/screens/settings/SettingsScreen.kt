package cz.mendelu.souvenirbox.ui.screens.settings

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen

@Composable
fun SettingsScreen(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: SettingsViewModel = hiltViewModel<SettingsViewModel>()
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

     BaseScreen(
        topBarText = "Settings",
        showLoading = false
    ) {
        SettingsScreenContent(
            paddingValues = it,
            actions = viewModel,
            state = state.value
        )
    }

}

@Composable
fun SettingsScreenContent(
    paddingValues: PaddingValues,
    actions: SettingsViewModel,
    state: SettingsUIState
) {
    val languages = listOf("English", "Slovak")
    var selectedLanguage by remember { mutableStateOf(languages.first()) }
    var languageExpanded by remember { mutableStateOf(false) }

    var currencyExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // dark mode
        SettingsSection(
            title = "Appearance"
        ) {
            SettingsSwitchRow(
                iconRes = R.drawable.dark_mode,
                label = "Dark theme",
                checked = state.darkTheme,
                onCheckedChange = {
                    actions.setTheme(it)
                }
            )
        }

        // language
        SettingsSection(
            title = "Preferences"
        ) {
            SettingsDropdownRow(
                iconRes = R.drawable.language,
                selected = selectedLanguage,
                expanded = languageExpanded,
                onExpandedChange = { languageExpanded = it },
                options = languages,
                onSelect = { lang ->
                    selectedLanguage = lang
                    languageExpanded = false
                    // viewModel.setLanguage(lang) // TODO
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // currency
            SettingsDropdownRow(
                iconRes = R.drawable.currency,
                selected = state.currency,
                expanded = currencyExpanded,
                onExpandedChange = { currencyExpanded = it },
                options = state.currencyOptions,
                readOnly = true,
                onSelect = { cur ->
                    actions.setCurrency(cur)
                    currencyExpanded = false
                }
            )
        }
    }
}

/* ---------------- UI building blocks ---------------- */

@Composable
private fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                content = content
            )
        }
    }
}

@Composable
private fun SettingsSwitchRow(
    @DrawableRes iconRes: Int,
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsDropdownRow(
    @DrawableRes iconRes: Int,
    selected: String,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    options: List<String>,
    readOnly: Boolean = true,
    query: String? = null,
    onQueryChange: (String) -> Unit = {},
    onSelect: (String) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.width(12.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { onExpandedChange(!expanded) },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = if (readOnly) selected else query!!,
                onValueChange = {
                    onQueryChange(it)
                    if (!expanded) {
                        onExpandedChange(true)
                    }
                },
                readOnly = readOnly,
                singleLine = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(
                        type = if (readOnly) ExposedDropdownMenuAnchorType.PrimaryNotEditable
                                else ExposedDropdownMenuAnchorType.PrimaryEditable,
                        enabled = true
                    ),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { onExpandedChange(false) }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onSelect(option)
                            onQueryChange(option)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }
    }
}
