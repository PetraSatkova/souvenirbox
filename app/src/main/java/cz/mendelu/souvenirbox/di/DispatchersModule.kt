package cz.mendelu.souvenirbox.di

import cz.mendelu.souvenirbox.dispatcher.AppDispatchers
import cz.mendelu.souvenirbox.dispatcher.DefaultAppDispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatchersModule {
    @Provides
    @Singleton
    fun provideDispatchers() : AppDispatchers {
        return DefaultAppDispatchers()
    }
}