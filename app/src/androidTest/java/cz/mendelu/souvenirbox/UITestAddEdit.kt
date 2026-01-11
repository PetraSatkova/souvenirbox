package cz.mendelu.souvenirbox

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import cz.mendelu.souvenirbox.fake.FakeAddEditActions
import cz.mendelu.souvenirbox.mock.ServerMock
import cz.mendelu.souvenirbox.fake.FakeSettingsActions
import cz.mendelu.souvenirbox.testTags.TestTagAddCurrencyDropdown
import cz.mendelu.souvenirbox.testTags.TestTagCurrencyDropdown
import cz.mendelu.souvenirbox.testTags.TestTagDarkThemeSwitch
import cz.mendelu.souvenirbox.testTags.TestTagDatePopup
import cz.mendelu.souvenirbox.ui.activities.MainActivity
import cz.mendelu.souvenirbox.ui.screens.addEditSouvenir.AddEditScreenContent
import cz.mendelu.souvenirbox.ui.screens.addEditSouvenir.AddEditUIState
import cz.mendelu.souvenirbox.ui.screens.settings.SettingsScreenContent
import cz.mendelu.souvenirbox.ui.screens.settings.SettingsUIState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestAddEdit {

    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_showsAddCurrencyDropdown() {
        val state = AddEditUIState(
            currencyList = ServerMock.currencyList.keys.toList()
        )

        composeRule.activity.setContent {
            AddEditScreenContent(
                paddingValues = PaddingValues(),
                state = state,
                actions = FakeAddEditActions()
            )
        }
        composeRule.onNode(hasText("Currency")).performClick()
        composeRule.onNodeWithTag(TestTagAddCurrencyDropdown).assertIsDisplayed()
    }

    @Test
    fun test_showsDatePopup() {
        val state = AddEditUIState(
            currencyList = ServerMock.currencyList.keys.toList()
        )

        composeRule.activity.setContent {
            AddEditScreenContent(
                paddingValues = PaddingValues(),
                state = state,
                actions = FakeAddEditActions()
            )
        }
        composeRule.onNode(hasText("Date")).performClick()
        composeRule.onNodeWithTag(TestTagDatePopup).assertIsDisplayed()
    }
}