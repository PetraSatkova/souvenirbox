package cz.mendelu.souvenirbox.communication

import javax.inject.Inject

class CurrencyRemoteRepositoryImpl @Inject constructor(private val api: CurrencyAPI):
    ICurrencyRemoteRepository {

}