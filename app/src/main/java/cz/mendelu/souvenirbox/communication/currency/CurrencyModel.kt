package cz.mendelu.souvenirbox.communication.currency

data class CurrencyModel(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)