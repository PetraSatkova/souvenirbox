package cz.mendelu.souvenirbox.navigation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardScreen
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
                navigation = navRouter
            )
        }

        composable(
            route = Destination.SouvenirsListScreen.route
        ) {
            SouvenirsListScreen(
                navigation = navRouter
            )
        }

//        composable(
//            route = Destination.SouvenirDetailScreen.route
//        ) {
//            SouvenirDetailScreen(
//                navigation = navRouter
//            )
//        }
//
//        composable(
//            route = Destination.AddEditSouvenirScreen.route
//        ) {
//            AddEditSouvenirScreen(
//                navigation = navRouter
//            )
//        }
//
//        composable(
//            route = Destination.MapScreen.route
//        ) {
//            MapScreen(
//                navigation = navRouter
//            )
//        }
//
//        composable(
//            route = Destination.SettingsScreen.route
//        ) {
//            SettingsScreen(
//                navigation = navRouter
//            )
//        }
    }
}
