package cz.mendelu.souvenirbox.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import cz.mendelu.souvenirbox.ui.screens.addEditSouvenir.AddEditScreen
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardScreen
import cz.mendelu.souvenirbox.ui.screens.map.MapScreen
import cz.mendelu.souvenirbox.ui.screens.settings.SettingsScreen
import cz.mendelu.souvenirbox.ui.screens.souvenirDetail.SouvenirDetailScreen
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListScreen

@ExperimentalFoundationApi
@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter,
    paddingValues: PaddingValues
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Destination.DashboardScreen.route
        ) {
            DashboardScreen(
                navigation = navRouter,
                paddingValues = paddingValues
            )
        }

        composable(
            route = Destination.SouvenirsListScreen.route
        ) {
            SouvenirsListScreen(
                navigation = navRouter,
                paddingValues = paddingValues
            )
        }

        composable(
            route = Destination.SouvenirDetailScreen.route,
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong("id")
            SouvenirDetailScreen(
                navigation = navRouter,
                id = id!!
            )
        }

        composable(
            route = Destination.AddEditSouvenirScreen.routeWithArgument,
            arguments = listOf(
                navArgument("id"){
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) { navBackStackEntry ->
            val id = navBackStackEntry.arguments?.getLong("id")
            AddEditScreen(
                navigation = navRouter,
                id = id
            )
        }

        composable(
            route = Destination.AddEditSouvenirScreen.route
        ) {
            AddEditScreen(
                navigation = navRouter,
                id = null
            )
        }

        composable(
            route = Destination.MapScreen.route
        ) {
            MapScreen(
                navigation = navRouter,
                paddingValues = paddingValues
            )
        }

        composable(
            route = Destination.SettingsScreen.route
        ) {
            SettingsScreen(
                navigation = navRouter,
                paddingValues = paddingValues
            )
        }
    }
}
