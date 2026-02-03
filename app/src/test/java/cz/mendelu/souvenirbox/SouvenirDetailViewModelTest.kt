package cz.mendelu.souvenirbox

import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.CurrencyModel
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.fake.FakeCurrencyRemoteRepository
import cz.mendelu.souvenirbox.fake.FakeDataStoreRepository
import cz.mendelu.souvenirbox.fake.FakeSouvenirsLocalRepository
import cz.mendelu.souvenirbox.fake.MainDispatcherRule
import cz.mendelu.souvenirbox.fake.TestAppDispatchers
import cz.mendelu.souvenirbox.ui.screens.souvenirDetail.SouvenirDetailViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import java.time.Instant
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class SouvenirDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenir null id does nothing`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )
        val vm = SouvenirDetailViewModel(
            souvenirsLocalRepository = FakeSouvenirsLocalRepository(),
            currencyRemoteRepository = FakeCurrencyRemoteRepository(),
            dataStore = FakeDataStoreRepository(),
            dispatchers = dispatchers
        )

        vm.loadSouvenir(null)
        advanceUntilIdle()

        // stays default
        assertTrue(vm.uiState.value.loading)
        assertNull(vm.uiState.value.souvenir)
        assertNull(vm.uiState.value.priceInMyCurrency)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenir when myCurrency equals souvenir currency sets priceInMyCurrency without calling rates`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )

        val souvenir = SouvenirEntity(
            id = 1,
            name = "Souvenir 1",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 10.0,
            currency = "EUR",
            date = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )

        val localRepo = FakeSouvenirsLocalRepository(souvenirById = souvenir)
        val remoteRepo = FakeCurrencyRemoteRepository(
            ratesResult = CommunicationResult.ConnectionError() // should NOT matter
        )
        val dataStore = FakeDataStoreRepository()

        val vm = SouvenirDetailViewModel(
            localRepo,
            remoteRepo,
            dataStore = dataStore,
            dispatchers = dispatchers
        )

        vm.loadSouvenir(1L)
        advanceUntilIdle()

        val s = vm.uiState.value
        assertFalse(s.loading)
        assertEquals(souvenir, s.souvenir)
        assertEquals("EUR", s.myCurrency)
        assertEquals(10.0, s.priceInMyCurrency!!, 0.000001)
        assertEquals(true, s.isFavourite)
        assertNull(s.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenir when currencies differ and rates success converts price`() = runTest {
        val souvenir = SouvenirEntity(
            id = 2,
            name = "Souvenir 2",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 10.0,
            currency = "CZK",
            date = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )

        val localRepo = FakeSouvenirsLocalRepository(souvenirById = souvenir)
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )

        // Suppose CZK to EUR rate is 25
        val currencyModel = CurrencyModel(
            base = "CZK",
            date = "2026-02-02",
            rates = mapOf("EUR" to 25.0)
        )

        val remoteRepo = FakeCurrencyRemoteRepository(
            ratesResult = CommunicationResult.Success(currencyModel)
        )

        val dataStore = FakeDataStoreRepository()

        val vm = SouvenirDetailViewModel(
            localRepo,
            remoteRepo,
            dataStore,
            dispatchers = dispatchers
        )

        vm.loadSouvenir(2L)
        advanceUntilIdle()

        val s = vm.uiState.value
        assertFalse(s.loading)
        assertEquals("EUR", s.myCurrency)
        assertEquals(250.00, s.priceInMyCurrency!!, 0.000001)
        assertNull(s.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `deleteSouvenir marks deleted and calls repository`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )

        val souvenir = SouvenirEntity(
            id = 3,
            name = "Souvenir 3",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 150.5,
            currency = "CZK",
            date = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )

        val localRepo = FakeSouvenirsLocalRepository(souvenirById = souvenir)
        val vm = SouvenirDetailViewModel(
            souvenirsLocalRepository = localRepo,
            currencyRemoteRepository = FakeCurrencyRemoteRepository(),
            dataStore = FakeDataStoreRepository(),
            dispatchers = dispatchers
        )

        vm.loadSouvenir(3L)
        advanceUntilIdle()

        vm.deleteSouvenir()
        advanceUntilIdle()

        assertTrue(vm.uiState.value.deleted)
        assertEquals(souvenir, localRepo.deleted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `updateFavourite toggles isFavourite and calls repository`() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val dispatchers = TestAppDispatchers(
            io = testDispatcher,
            main = testDispatcher
        )

        val souvenir = SouvenirEntity(
            id = 4,
            name = "Souvenir 4",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 150.5,
            currency = "CZK",
            date = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli(),
            isFavourite = false,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )

        val localRepo = FakeSouvenirsLocalRepository(souvenirById = souvenir)
        val vm = SouvenirDetailViewModel(
            souvenirsLocalRepository = localRepo,
            currencyRemoteRepository = FakeCurrencyRemoteRepository(),
            dataStore = FakeDataStoreRepository(),
            dispatchers = dispatchers
        )

        vm.loadSouvenir(4L)
        advanceUntilIdle()

        assertFalse(vm.uiState.value.isFavourite)

        vm.updateFavourite(4L)
        advanceUntilIdle()

        assertEquals(4L, localRepo.updatedFavouriteId)
        assertTrue(vm.uiState.value.isFavourite)
    }
}
