package cz.mendelu.souvenirbox.fake

import cz.mendelu.souvenirbox.communication.CommunicationResult
import cz.mendelu.souvenirbox.communication.currency.CurrencyModel
import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository
import cz.mendelu.souvenirbox.mock.ServerMock
import javax.inject.Inject


class FakeCurrencyRemoteRepositoryImpl @Inject constructor(): ICurrencyRemoteRepository {

    override suspend fun getRates(
        base: String,
        symbols: String
    ): CommunicationResult<CurrencyModel> {
        return CommunicationResult.Success(data = ServerMock.currency)
    }

    override suspend fun getCurrencies(): CommunicationResult<Map<String, String>> {
        return CommunicationResult.Success(data = ServerMock.currencyList)

    }
}