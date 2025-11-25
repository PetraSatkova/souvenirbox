package cz.mendelu.souvenirbox.navigation

import androidx.navigation.NavController

class NavigationRouterImpl(private val navController: NavController) : INavigationRouter {
    override fun navigateToSouvenirsList() {
        navController.navigate(route = Destination.SouvenirsListScreen.route)
    }

    override fun navigateToSouvenirDetail(id: Long) {
        navController.navigate(route = Destination.SouvenirDetailScreen.route)
    }

    override fun navigateToAddEdit(id: Long?) {
        navController.navigate(route = Destination.AddEditSouvenirScreen.route)
    }

    override fun navigateToMap() {
        navController.navigate(route = Destination.MapScreen.route)
    }

    override fun navigateToSettings() {
        navController.navigate(route = Destination.SettingsScreen.route)
    }

    override fun getNavController(): NavController = navController

    override fun returnBack() {
        navController.popBackStack()
    }
}