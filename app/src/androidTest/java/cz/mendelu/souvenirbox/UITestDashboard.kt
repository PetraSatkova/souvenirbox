package cz.mendelu.souvenirbox

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
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirCarousel
import cz.mendelu.souvenirbox.ui.activities.MainActivity
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardScreenContent
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardUIState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestDashboard {

//    private lateinit var navController: NavHostController

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun test_showsNoSouvenirsText_whenRecentSouvenirsEmpty() {
        val state = DashboardUIState(
            loading = false
        )

        launchDashboardScreen(state = state)

        Thread.sleep(1000)

        composeRule.onNodeWithTag(TestTagNoSouvenirs).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagSouvenirCarousel+"1").assertDoesNotExist()
        Thread.sleep(1000)

    }

    @Test
    fun test_showsCarousel_whenRecentSouvenirsNotEmpty () {
        val state = DashboardUIState(
            recentSouvenirs = DatabaseMock.souvenirList,
            favouriteSouvenirs = DatabaseMock.favouriteSouvenirs,
            loading = false
        )

        launchDashboardScreen(state = state)

        Thread.sleep(1000)

        composeRule.onNodeWithTag(TestTagNoSouvenirs).assertDoesNotExist()
        composeRule.onNodeWithTag(TestTagSouvenirCarousel+"1").assertIsDisplayed()
        Thread.sleep(1000)

    }

    private fun launchDashboardScreen(state: DashboardUIState) {
        composeRule.setContent {
            DashboardScreenContent(
                paddingValuesTop = androidx.compose.foundation.layout.PaddingValues(),
                state = state,
                navigation = FakeNavRouter()
            )
        }
    }
}