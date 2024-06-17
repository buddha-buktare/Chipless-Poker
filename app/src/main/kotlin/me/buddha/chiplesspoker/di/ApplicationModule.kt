package me.buddha.chiplesspoker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.GameRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationModule {

    @Provides
    @Singleton
    fun provideGameRepository(

    ): GameRepository

}