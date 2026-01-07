package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.communication.currency.ICurrencyRemoteRepository
import cz.mendelu.souvenirbox.fake.FakeCurrencyRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RemoteRepositoryModule::class]
)
abstract class FakeRemoteRepositoryModule {
    @Binds
    abstract fun provideCurrencyRemoteRepository(
        repository: FakeCurrencyRemoteRepositoryImpl
    ): ICurrencyRemoteRepository

}