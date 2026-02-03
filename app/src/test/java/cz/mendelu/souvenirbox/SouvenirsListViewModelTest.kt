package cz.mendelu.souvenirbox

import app.cash.turbine.test
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.fake.FakeSouvenirsLocalRepository
import cz.mendelu.souvenirbox.fake.MainDispatcherRule
import cz.mendelu.souvenirbox.ui.screens.souvenirsList.SouvenirsListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class SouvenirsListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `loadSouvenirs - initial then updates with sorted list`() = runTest {
        val repo = FakeSouvenirsLocalRepository()
        val vm = SouvenirsListViewModel(repo)

        val sOld = SouvenirEntity(
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
        val sNew = SouvenirEntity(
            id = 2,
            name = "Souvenir 2",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 10.0,
            currency = "EUR",
            date = Instant.parse("2026-01-03T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )
        val sMid = SouvenirEntity(
            id = 3,
            name = "Souvenir 3",
            latitude = 49.16,
            longitude = 52.6,
            city = "Brno",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 10.0,
            currency = "EUR",
            date = Instant.parse("2026-01-02T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag1", "tag2")
        )

        vm.uiState.test {
            // initial StateFlow value
            val initial = awaitItem()
            assertTrue(initial.loading)
            assertEquals(null, initial.souvenirs)

            vm.loadSouvenirs()
            advanceUntilIdle()

            // 1) emission caused by initial repo value (emptyList)
            val afterStart = awaitItem()
            assertEquals(false, afterStart.loading)
            assertEquals(emptyList<SouvenirEntity>(), afterStart.souvenirs)

            // 2) emission caused by your explicit list
            repo.setSouvenirs(listOf(sOld, sNew, sMid))
            advanceUntilIdle()

            val updated = awaitItem()
            assertEquals(false, updated.loading)
            assertEquals(listOf(sNew, sMid, sOld), updated.souvenirs)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
