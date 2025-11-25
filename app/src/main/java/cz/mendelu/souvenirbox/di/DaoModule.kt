package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.database.SouvenirsDao
import cz.mendelu.souvenirbox.database.SouvenirsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {
    @Provides
    @Singleton
    fun provideDao(database: SouvenirsDatabase) : SouvenirsDao {
        return database.souvenirsDao()
    }
}