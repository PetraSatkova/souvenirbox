package cz.mendelu.souvenirbox

import app.cash.turbine.test
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.fake.FakeSouvenirsLocalRepository
import cz.mendelu.souvenirbox.fake.MainDispatcherRule
import cz.mendelu.souvenirbox.ui.screens.dashboard.DashboardViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import java.time.Instant

class DashboardViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init - starts loading then updates state from repository`() = runTest {
        val repo = FakeSouvenirsLocalRepository()
        val vm = DashboardViewModel(repo)

        // sample data (adjust construction to your real SouvenirEntity)
        val s1 = SouvenirEntity(
            id = 1,
            name = "Souvenir 1",
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
        val s2 = SouvenirEntity(
            id = 2,
            name = "Souvenir 3",
            latitude = 41.16,
            longitude = 52.6,
            city = "Praha",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 300.0,
            currency = "CZK",
            date = Instant.parse("2026-01-02T10:00:00Z").toEpochMilli(),
            isFavourite = false,
            imageUri = "",
            notes = "best one ever",
            tags = listOf("tag2", "tag3")
        )
        val s3 = SouvenirEntity(
            id = 3,
            name = "Souvenir 3",
            latitude = 30.16,
            longitude = 20.6,
            city = "Barcelona",
            country = "Spain",
            countryCode = "S",
            price = 10.5,
            currency = "EUR",
            date = Instant.parse("2026-01-03T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "",
            tags = listOf("tag5", "tag6")
        )

        vm.uiState.test {
            // 1) initial state from StateFlow
            val initial = awaitItem()
            Assert.assertEquals(emptyList<SouvenirEntity>(), initial.recentSouvenirs)
            Assert.assertEquals(emptyList<SouvenirEntity>(), initial.favouriteSouvenirs)

            // 2) emit list from repo
            repo.setSouvenirs(listOf(s1, s2, s3))
            advanceUntilIdle()

            val updated = awaitItem()
            Assert.assertEquals(false, updated.loading)

            // recentSouvenirs sorted by date desc
            Assert.assertEquals(listOf(s3, s2, s1), updated.recentSouvenirs)

            // favourites filtered
            Assert.assertEquals(listOf(s1, s3), updated.favouriteSouvenirs)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `init - reacts to subsequent repository emissions`() = runTest {
        val repo = FakeSouvenirsLocalRepository()
        val vm = DashboardViewModel(repo)

        val old = SouvenirEntity(
            id = 1,
            name = "Souvenir 1",
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
        val fav = SouvenirEntity(
            id = 2,
            name = "Souvenir 3",
            latitude = 41.16,
            longitude = 52.6,
            city = "Praha",
            country = "Czech Republic",
            countryCode = "CZ",
            price = 300.0,
            currency = "CZK",
            date = Instant.parse("2026-01-02T10:00:00Z").toEpochMilli(),
            isFavourite = true,
            imageUri = "",
            notes = "best one ever",
            tags = listOf("tag2", "tag3")
        )

        vm.uiState.test {
            awaitItem() // initial loading=true

            repo.setSouvenirs(listOf(old))
            advanceUntilIdle()
            val first = awaitItem()
            Assert.assertEquals(listOf(old), first.recentSouvenirs)
            Assert.assertEquals(emptyList<SouvenirEntity>(), first.favouriteSouvenirs)
            Assert.assertEquals(false, first.loading)

            repo.setSouvenirs(listOf(old, fav))
            advanceUntilIdle()
            val second = awaitItem()
            Assert.assertEquals(listOf(fav, old), second.recentSouvenirs) // date desc
            Assert.assertEquals(listOf(fav), second.favouriteSouvenirs)
            Assert.assertEquals(false, second.loading)
        }
    }
}