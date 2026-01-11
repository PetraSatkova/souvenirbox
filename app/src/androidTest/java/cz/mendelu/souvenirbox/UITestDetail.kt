package cz.mendelu.souvenirbox

import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import cz.mendelu.souvenirbox.fake.FakeDetailActions
import cz.mendelu.souvenirbox.mock.DatabaseMock
import cz.mendelu.souvenirbox.mock.ServerMock
import cz.mendelu.souvenirbox.navigation.Destination
import cz.mendelu.souvenirbox.fake.FakeNavRouter
import cz.mendelu.souvenirbox.navigation.NavGraph
import cz.mendelu.souvenirbox.testTags.TestTagPlaceholder
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirDetailLocalPrice
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirDetailMyPrice
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirDetailTags
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirList
import cz.mendelu.souvenirbox.ui.activities.MainActivity
import cz.mendelu.souvenirbox.ui.screens.souvenirDetail.SouvenirDetailScreenContent
import cz.mendelu.souvenirbox.ui.screens.souvenirDetail.SouvenirDetailUIState
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListScreenContent
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListUIState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert
import org.junit.Before
import org.junit.FixMethodOrder
import org.junit.Rule
import org.junit.Test
import org.junit.runners.MethodSorters
import java.math.BigDecimal
import java.math.RoundingMode

@HiltAndroidTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class UITestDetail {

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
    fun test_showsImageMLTags_Prices() {
        val state = SouvenirDetailUIState(
            loading = false,
            souvenir = DatabaseMock.souvenir1,
            myCurrency = "AUD",
            priceInMyCurrency = BigDecimal(
                ServerMock.currency.rates["AUD"]?.times(DatabaseMock.souvenir1.price)!!)
                .setScale(2, RoundingMode.HALF_UP)
                .toDouble()
        )

        composeRule.activity.setContent {
            SouvenirDetailScreenContent(
                paddingValues = PaddingValues(),
                state = state,
                actions = FakeDetailActions()
            )
        }

        composeRule.onNodeWithTag(TestTagSouvenirDetailTags).assertIsDisplayed()
        composeRule.onNodeWithTag(TestTagSouvenirDetailLocalPrice)
            .assertIsDisplayed()
            .assertTextContains("100", true)
        composeRule.onNodeWithTag(TestTagSouvenirDetailMyPrice)
            .assertIsDisplayed()
            .assertTextContains("175.08", true)
    }



//    @OptIn(ExperimentalAnimationApi::class, ExperimentalFoundationApi::class)
//    private fun launchListOSouvenirsScreenWithNavigation() {
//        composeRule.activity.setContent {
//            MaterialTheme {
//                navController = rememberNavController()
//                NavGraph(
//                    navController = navController,
//                    startDestination = Destination.SouvenirsListScreen.route,
//                    navRouter = FakeNavRouter(),
//                    paddingValues = PaddingValues(),
//                )
//            }
//        }
//    }

//    @Test
//    fun test_list_of_souvenirs_exists() {
//        launchListOSouvenirsScreenWithNavigation()
//        with(composeRule) {
//            onNodeWithTag(TestTagPlaceholder).assertDoesNotExist()
//            onNodeWithTag(TestTagSouvenirList).assertIsDisplayed()
//        }
//    }
//
//    @Test
//    fun test_navigate_to_correct_pet_detail_screen() {
//        launchListOSouvenirsScreenWithNavigation()
//        with(composeRule) {
//            val targetSouvenir = DatabaseMock.souvenirList.first { it.name == "Souvenir1" }
//            assert(targetSouvenir.id != null)
//            onNodeWithTag(TestTagSouvenirList).isDisplayed()
//
//            onNode(hasText(targetSouvenir.name)).assertIsDisplayed()
//            onNode(hasText(targetSouvenir.name)).performClick()
//            waitForIdle()
//
//            val route = navController.currentBackStackEntry?.destination?.route
//            Assert.assertTrue(route == Destination.SouvenirDetailScreen.route)
//        }
//    }

}