package cz.mendelu.souvenirbox.communication.currency

import cz.mendelu.souvenirbox.communication.CommunicationResult

interface ICurrencyRemoteRepository {
    suspend fun getRates(base: String, symbols: String): CommunicationResult<CurrencyModel>
    suspend fun getCurrencies(): CommunicationResult<Map<String, String>>
}