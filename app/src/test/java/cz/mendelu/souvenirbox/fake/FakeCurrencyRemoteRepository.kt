package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.CurrencyModel
import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository

class FakeCurrencyRemoteRepository(
    private val currenciesResult: CommunicationResult<Map<String, String>> =
        CommunicationResult.Success(emptyMap()),
    private val ratesResult: CommunicationResult<CurrencyModel> =
        CommunicationResult.Success(CurrencyModel(
            base = "EUR",
            date = "2020-01-01",
            rates = mapOf("AUD" to 1.7)
        ))
) : ICurrencyRemoteRepository {

    override suspend fun getCurrencies(): CommunicationResult<Map<String, String>> {
        return currenciesResult
    }

    override suspend fun getRates(base: String, symbols: String): CommunicationResult<CurrencyModel> {
        return ratesResult
    }
}
