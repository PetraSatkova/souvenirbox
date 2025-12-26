package cz.mendelu.souvenirbox.communication.currency

import cz.mendelu.souvenirbox.communication.currency.CurrencyModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CurrencyAPI {
    @Headers("Content-Type: application/json")
    @GET("latest")
    suspend fun getRates(
        @Query("base") base: String,
        @Query("symbols") symbols: String
    ): Response<CurrencyModel>
}