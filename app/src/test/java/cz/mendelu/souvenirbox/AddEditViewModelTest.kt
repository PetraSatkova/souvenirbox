package cz.mendelu.souvenirbox

import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.CurrencyModel
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.fake.FakeCurrencyRemoteRepository
import cz.mendelu.souvenirbox.fake.FakeImageTaggerFacade
import cz.mendelu.souvenirbox.fake.FakeSouvenirsLocalRepository
import cz.mendelu.souvenirbox.fake.MainDispatcherRule
import cz.mendelu.souvenirbox.ui.screens.addEditSouvenir.AddEditViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class AddEditViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenir with id loads entity into uiState`() = runTest {
        val entity = SouvenirEntity(
            id = 7,
            name = "S1",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 10.0,
            currency = "CZK",
            date = Instant.parse("2026-01-01T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "uri",
            notes = "note",
            tags = listOf("t1", "t2")
        )

        val local = FakeSouvenirsLocalRepository(souvenirById = entity)
        val currencyModel = CurrencyModel(
            base = "CZK",
            date = "2026-02-02",
            rates = mapOf("EUR" to 25.0)
        )

        val remoteRepo = FakeCurrencyRemoteRepository(
            ratesResult = CommunicationResult.Success(currencyModel)
        )

        val vm = AddEditViewModel(
            souvenirsLocalRepository = local,
            currencyRemoteRepository = remoteRepo,
            imageTagger = FakeImageTaggerFacade(listOf("beach", "sunset"))
        )


        vm.loadSouvenir(7L)
        advanceUntilIdle()

        val s = vm.uiState.value
        assertEquals(7L, s.id)
        assertEquals("S1", s.name)
        assertEquals(49.16, s.latitude!!, 0.0)
        assertEquals(52.6, s.longitude!!, 0.0)
        assertEquals("Brno", s.city)
        assertEquals("Czech Republic", s.country)
        assertEquals("CZ", s.countryCode)
        assertEquals("10.0", s.price)
        assertEquals("CZK", s.currency)
        assertEquals(entity.date, s.date)
        assertEquals("note", s.notes)
        assertEquals("uri", s.imageUri)
        assertEquals(listOf("t1", "t2"), s.tags)

        // and it should not set an error in this branch
        assertNull(s.error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenir with null id loads currencyList on success`() = runTest {
        val local = FakeSouvenirsLocalRepository()
        val currencyModel = CurrencyModel(
            base = "CZK",
            date = "2026-02-02",
            rates = mapOf("EUR" to 25.0)
        )

        val remoteRepo = FakeCurrencyRemoteRepository(
            ratesResult = CommunicationResult.Success(currencyModel)
        )

        val vm = AddEditViewModel(
            souvenirsLocalRepository = local,
            currencyRemoteRepository = remoteRepo,
            imageTagger = FakeImageTaggerFacade(listOf("beach", "sunset"))
        )

        vm.loadSouvenir(null)
        advanceUntilIdle()

        val s = vm.uiState.value
        assertNull(s.error)
    }
}
