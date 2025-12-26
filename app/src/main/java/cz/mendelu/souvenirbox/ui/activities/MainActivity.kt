package cz.mendelu.souvenirbox.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import cz.mendelu.souvenirbox.datastore.IDataStoreRepository
import cz.mendelu.souvenirbox.ui.screens.MainScreen
import cz.mendelu.souvenirbox.ui.theme.SouvenirBoxTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject lateinit var dataStore: IDataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val darkTheme by dataStore.darkThemeFlow.collectAsState(initial = false)

            SouvenirBoxTheme(darkTheme = darkTheme) {
                MainScreen()
            }
        }
    }
}
