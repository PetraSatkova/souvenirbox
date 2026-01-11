package cz.mendelu.souvenirbox

import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import cz.mendelu.souvenirbox.mock.DatabaseMock
import cz.mendelu.souvenirbox.navigation.Destination
import cz.mendelu.souvenirbox.fake.FakeNavRouter
import cz.mendelu.souvenirbox.navigation.NavigationRouterImpl
import cz.mendelu.souvenirbox.testTags.TestTagNoSouvenirs
import cz.mendelu.souvenirbox.testTags.TestTagPlaceholder
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirCarousel
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirList
import cz.mendelu.souvenirbox.ui.activities.MainActivity
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardScreenContent
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardUIState
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListScreen
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListScreenContent
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListUIState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestList {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_showsPlaceholder_whenNoSouvenirsSaved() {
        val state = MutableStateFlow(
            SouvenirsListUIState(
                loading = false,
                souvenirs = emptyList()
            )
        )

        composeRule.activity.setContent {
            SouvenirsListScreen(
                navigation = FakeNavRouter(),
                paddingValues = PaddingValues(),
                testState = state
            )
        }

        composeRule.onNodeWithTag(TestTagPlaceholder).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagSouvenirList).assertDoesNotExist()
    }

    @Test
    fun test_showsList_whenSouvenirsNotEmpty () {
        val state = MutableStateFlow(
            SouvenirsListUIState(
                loading = false,
                souvenirs = DatabaseMock.souvenirList
            )
        )

        composeRule.activity.setContent {
            SouvenirsListScreen(
                navigation = FakeNavRouter(),
                paddingValues = PaddingValues(),
                testState = state
            )
        }

        composeRule.onNodeWithTag(TestTagPlaceholder).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTagSouvenirList).assertIsDisplayed()

    }
}