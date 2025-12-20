package cz.mendelu.souvenirbox.ui.screens.settings

import androidx.lifecycle.ViewModel
import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val souvenirsLocalRepository: ISouvenirsLocalRepository
) : ViewModel()
{
    fun changeTheme() {

    }
}