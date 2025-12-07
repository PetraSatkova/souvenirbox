package cz.mendelu.souvenirbox.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import cz.mendelu.souvenirbox.R

@Composable
fun BottomNavigationBar(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val navigationItems = listOf(
        NavigationItem(
            title = "Dashboard",
            icon = R.drawable.dashboard,
            route = Destination.DashboardScreen.route
        ),
        NavigationItem(
            title = "Souvenirs",
            icon = R.drawable.menu,
            route = Destination.SouvenirsListScreen.route
        ),
        NavigationItem(
            title = "Map",
            icon = R.drawable.location,
            route = Destination.MapScreen.route
        ),
        NavigationItem(
            title = "Settings",
            icon = R.drawable.settings,
            route = Destination.SettingsScreen.route
        )
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        navigationItems.forEachIndexed { index, item ->
            val selected = currentDestination
                ?.hierarchy
                ?.any { it.route == item.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        launchSingleTop = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = {
                    Text(
                        item.title,
                        color = Color.Black
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.surface,
                    indicatorColor = colorResource(R.color.app_blue)
                )

            )
        }
    }
}

data class NavigationItem(
    val title: String,
    val icon: Int,
    val route: String
)