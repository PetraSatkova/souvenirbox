package cz.mendelu.souvenirbox.navigation

sealed class Destination(
    val route: String
){
    object DashboardScreen: Destination("dashboard")
    object SouvenirsListScreen: Destination("souvenirsList")
    object SouvenirDetailScreen: Destination("souvenirDetail/{id}") {
        fun getRouteWithArgument(id: Long) = "souvenirDetail/$id"
    }
    object AddEditSouvenirScreen: Destination("addEditSouvenir") {
        const val routeWithArgument: String = "addEditSouvenir/{id}"
        fun getRouteWithArgument(id: Long) = "addEditSouvenir/$id"
    }
    object MapScreen: Destination("map")
    object SettingsScreen: Destination("settings")
}
