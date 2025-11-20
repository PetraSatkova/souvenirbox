package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.communication.CurrencyAPI
import cz.mendelu.souvenirbox.communication.CurrencyRemoteRepositoryImpl
import cz.mendelu.souvenirbox.communication.ICurrencyRemoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteRepositoryModule {

    @Provides
    @Singleton
    fun provideCurrencyRemoteRepository(currencyAPI: CurrencyAPI): ICurrencyRemoteRepository =
        CurrencyRemoteRepositoryImpl(currencyAPI)
}