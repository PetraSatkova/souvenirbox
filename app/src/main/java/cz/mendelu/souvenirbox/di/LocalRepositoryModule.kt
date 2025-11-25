package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.database.ISouvenirsLocalRepository
import cz.mendelu.souvenirbox.database.SouvenirsDao
import cz.mendelu.souvenirbox.database.SouvenirsLocalRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalRepositoryModule {

    @Provides
    @Singleton
    fun provideRepository(dao: SouvenirsDao) : ISouvenirsLocalRepository {
        return SouvenirsLocalRepositoryImpl(dao)
    }
}