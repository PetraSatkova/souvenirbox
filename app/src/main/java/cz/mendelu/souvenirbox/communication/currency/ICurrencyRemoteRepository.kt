package cz.mendelu.souvenirbox.communication.currency

import cz.petstore2025.communication.CommunicationResult

interface ICurrencyRemoteRepository {
    suspend fun getRates(base: String, symbols: String): CommunicationResult<CurrencyModel>
}