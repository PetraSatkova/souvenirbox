package cz.mendelu.souvenirbox.communication.model

data class CurrencyModel(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)