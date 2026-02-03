package cz.mendelu.souvenirbox

import app.cash.turbine.test
import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.fake.FakeCurrencyRemoteRepository
import cz.mendelu.souvenirbox.fake.FakeDataStoreRepository
import cz.mendelu.souvenirbox.fake.MainDispatcherRule
import cz.mendelu.souvenirbox.fake.TestAppDispatchers
import cz.mendelu.souvenirbox.ui.screens.settings.SettingsViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class SettingsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init - uiState reflects darkThemeFlow and currencyFlow`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)

        val dataStore = FakeDataStoreRepository()
        val api = FakeCurrencyRemoteRepository(
            currenciesResult = CommunicationResult.Success(mapOf("EUR" to "Euro"))
        )

        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )

        val vm = SettingsViewModel(api, dataStore, dispatchers)

        vm.uiState.test {
            // initial
            val initial = awaitItem()
            Assert.assertEquals(false, initial.darkTheme)
            Assert.assertEquals("EUR", initial.currency)

            // update datastore -> should propagate
            dataStore.setDarkTheme(true)
            dataStore.setCurrency("USD")

            advanceUntilIdle()

            // The stateflow may emit twice (theme then currency), so just grab latest:
            val s1 = awaitItem()
            val s2 = awaitItem()

            Assert.assertEquals(true, s2.darkTheme)
            Assert.assertEquals("USD", s2.currency)

            cancelAndIgnoreRemainingEvents()
        }
    }
}