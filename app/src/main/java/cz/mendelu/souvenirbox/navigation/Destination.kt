package cz.mendelu.souvenirbox.navigation

sealed class Destination(
    val route: String
){
    object DashboardScreen: Destination("dashboard")
    object SouvenirsListScreen: Destination("souvenirsList")
    object SouvenirDetailScreen: Destination("souvenirDetail")
    object AddEditSouvenirScreen: Destination("addEditSouvenir")
    object MapScreen: Destination("map")
    object SettingsScreen: Destination("settings")
}
