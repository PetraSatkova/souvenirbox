package cz.mendelu.souvenirbox.model

data class CurrencyModel(
    val base: String,
    val date: String,
    val rates: Map<String, Double>
)