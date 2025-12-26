package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.communication.currency.CurrencyAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object APIModule {

    @Provides
    @Singleton
    fun providePetsAPI(retrofit: Retrofit): CurrencyAPI =
        retrofit.create(CurrencyAPI::class.java)
}