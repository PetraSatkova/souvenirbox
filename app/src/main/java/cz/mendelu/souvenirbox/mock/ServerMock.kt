package cz.mendelu.souvenirbox.mock

import cz.mendelu.souvenirbox.communication.currency.CurrencyModel

object ServerMock {

    val currency = CurrencyModel(
        base = "EUR",
        date = "2026-01-02",
        rates = mapOf("AUD" to 1.7508)
    )

    val currencyList = mapOf(
        "AUD" to "Australian Dollar",
        "BRL" to "Brazilian Real",
        "CAD" to "Canadian Dollar",
        "CHF" to "Swiss Franc",
    )
}