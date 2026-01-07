package cz.mendelu.souvenirbox.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToSouvenirsList() {
        navController.navigate(route = Destination.SouvenirsListScreen.route)
    }

    override fun navigateToSouvenirDetail(id: Long) {
        navController.navigate(route = Destination.SouvenirDetailScreen.getRouteWithArgument(id)) {
            launchSingleTop = true
        }
    }

    override fun navigateToAddEdit(id: Long?) {
        if (id != null) {
            navController.navigate(route = Destination.AddEditSouvenirScreen.getRouteWithArgument(id)) {
                launchSingleTop = true
            }
        } else {
            navController.navigate(route = Destination.AddEditSouvenirScreen.route) {
                launchSingleTop = true
            }
        }
    }

    override fun navigateToMap() {
        navController.navigate(route = Destination.MapScreen.route)
    }

    override fun navigateToSettings() {
        navController.navigate(route = Destination.SettingsScreen.route)
    }

    override fun returnBack() {
        navController.popBackStack()
    }
}