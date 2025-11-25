package cz.mendelu.souvenirbox.di

import android.content.Context
import cz.mendelu.souvenirbox.database.SouvenirsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : SouvenirsDatabase {
        return SouvenirsDatabase.getDatabase(context)
    }
}