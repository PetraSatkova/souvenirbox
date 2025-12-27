package cz.mendelu.souvenirbox.communication.currency

import cz.mendelu.souvenirbox.communication.IBaseRemoteRepository
import cz.mendelu.souvenirbox.communication.CommunicationResult
import javax.inject.Inject

class CurrencyRemoteRepositoryImpl @Inject constructor(private val api: CurrencyAPI):
    ICurrencyRemoteRepository, IBaseRemoteRepository
{
    override suspend fun getRates(base: String, symbols: String): CommunicationResult<CurrencyModel> {
        return processResponse {
            api.getRates(
                base = base,
                symbols = symbols
            )
        }
    }

    override suspend fun getCurrencies(): CommunicationResult<Map<String, String>> {
        return processResponse {
            api.getCurrencies()
        }
    }

}