package cz.mendelu.souvenirbox.navigation

import androidx.navigation.NavController

interface INavigationRouter {
    fun navigateToSouvenirsList()
    fun navigateToSouvenirDetail(id: Long)
    fun navigateToAddEdit(id: Long?)
    fun navigateToMap()
    fun navigateToSettings()
    fun getNavController(): NavController
    fun returnBack()

}