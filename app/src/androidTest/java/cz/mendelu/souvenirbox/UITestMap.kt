package cz.mendelu.souvenirbox

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import cz.mendelu.souvenirbox.fake.FakeNavRouter
import cz.mendelu.souvenirbox.mock.ServerMock
import cz.mendelu.souvenirbox.fake.FakeSettingsActions
import cz.mendelu.souvenirbox.mock.DatabaseMock
import cz.mendelu.souvenirbox.testTags.TestTagCurrencyDropdown
import cz.mendelu.souvenirbox.testTags.TestTagDarkThemeSwitch
import cz.mendelu.souvenirbox.testTags.TestTagMapRoot
import cz.mendelu.souvenirbox.ui.activities.MainActivity
import cz.mendelu.souvenirbox.ui.screens.map.MapScreenContent
import cz.mendelu.souvenirbox.ui.screens.map.MapUIState
import cz.mendelu.souvenirbox.ui.screens.map.SouvenirSheetContent
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
class UITestMap {

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
    fun test_map_displaysRootContainer() {
        val state = MapUIState(
            loading = false,
            souvenirs = emptyList()
        )

        composeRule.activity.setContent {
            MapScreenContent(
                paddingValuesBottom = PaddingValues(),
                paddingValuesTop = PaddingValues(),
                state = state,
                navigation = FakeNavRouter()
            )
        }

        composeRule.onNodeWithTag(TestTagMapRoot).assertIsDisplayed()
    }


    @Test
    fun test_souvenirSheet_showsNameLocationPriceAndTags() {
        val souvenir = DatabaseMock.souvenir1
        composeRule.activity.setContent {
            SouvenirSheetContent(souvenir = souvenir)
        }

        // name is uppercased
        composeRule.onNodeWithText("SOUVENIR1").assertIsDisplayed()
        composeRule.onNodeWithText("Great Britain", substring = true).assertIsDisplayed()

        // price chip
        composeRule.onNodeWithText("100 EUR", substring = true).assertIsDisplayed()

        // tags"
        composeRule.onNodeWithText("#tag1").assertIsDisplayed()
        composeRule.onNodeWithText("#tag2").assertIsDisplayed()

        // notes
        composeRule.onNodeWithText("Souvenir1 notes", substring = true).assertIsDisplayed()
    }
}