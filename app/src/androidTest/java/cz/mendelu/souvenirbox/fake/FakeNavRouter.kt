package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.navigation.INavigationRouter

class FakeNavRouter (
): INavigationRouter {
    override fun navigateToSouvenirsList() {
    }

    override fun navigateToSouvenirDetail(id: Long) {
    }

    override fun navigateToAddEdit(id: Long?) {
    }

    override fun navigateToMap() {
    }

    override fun navigateToSettings() {
    }

    override fun returnBack() {
    }

}