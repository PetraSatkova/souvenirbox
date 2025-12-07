package cz.mendelu.souvenirbox.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.navigation.BottomNavigationBar
import cz.mendelu.souvenirbox.navigation.Destination
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.navigation.NavGraph
import cz.mendelu.souvenirbox.navigation.NavigationRouterImpl
import cz.mendelu.souvenirbox.ui.theme.halfMargin

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    navHostController: NavHostController = rememberNavController(),
    navRouter: INavigationRouter = remember {
        NavigationRouterImpl(navHostController)
    }
) {
    val bottomBarRoutes = listOf(
        Destination.DashboardScreen.route,
        Destination.SouvenirsListScreen.route,
        Destination.MapScreen.route,
        Destination.SettingsScreen.route
    )

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            if (currentRoute(navHostController) in bottomBarRoutes) {
                BottomNavigationBar(navController = navHostController)
            }
        },
        floatingActionButton = {
            if (currentRoute(navHostController) == Destination.SouvenirsListScreen.route) {
                ExtendedFloatingActionButton(
                    onClick = {
                        navRouter.navigateToAddEdit(id = null)
                    },
                    containerColor = colorResource(R.color.app_tyrkys)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "add souvenir"
                    )

                    Spacer(modifier = Modifier.width(halfMargin()))

                    Text(text = "Add Souvenir")
                }
            }
        }
    ) {
        NavGraph(
            startDestination = Destination.DashboardScreen.route,
            navController = navHostController,
            navRouter = navRouter,
            paddingValues = it
        )
    }
}

@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}