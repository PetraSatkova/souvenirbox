package cz.mendelu.souvenirbox.mock

import cz.mendelu.souvenirbox.database.SouvenirEntity

object DatabaseMock {

    val souvenir1 = SouvenirEntity(
        id = 1,
        name = "Souvenir1",
        latitude = 51.502150,
        longitude = -0.100757,
        city = "London",
        country = "Great Britain",
        countryCode = "GB",
        price = 100.0,
        currency = "EUR",
        date = 1672531200000,
        isFavourite = true,
        notes = "Souvenir1 notes",
        imageUri = null,
        tags = listOf("tag1", "tag2")
    )

    val souvenir2 = SouvenirEntity(
        id = 1,
        name = "Souvenir2",
        latitude = 49.815273,
        longitude = 19.040236,
        city = "Olomouc",
        country = "Czech Republic",
        countryCode = "CZ",
        price = 100.0,
        currency = "CZK",
        date = 1672531200000,
        isFavourite = false,
        notes = "Souvenir1 notes",
        imageUri = null,
        tags = listOf("tag1", "tag2")
    )

    val souvenir3 = SouvenirEntity(
        id = 1,
        name = "Souvenir3",
        latitude = 48.748666,
        longitude = 17.830210,
        city = "Nové Mesto nad Váhom",
        country = "Slovak Republic",
        countryCode = "EUR",
        price = 100.0,
        currency = "EUR",
        date = 1672531200000,
        isFavourite = true,
        notes = "Souvenir1 notes",
        imageUri = null,
        tags = listOf("tag1", "tag2")
    )

    val souvenirList = listOf(
        souvenir1,
        souvenir2,
        souvenir3
    )

    val favouriteSouvenirs = listOf(
        souvenir1,
        souvenir3
    )
}