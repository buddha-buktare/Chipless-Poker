package me.buddha.chiplesspoker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.local.GameDao
import me.buddha.chiplesspoker.data.repository.GameRepository
import me.buddha.chiplesspoker.data.repository.GameRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGameRepository(
        gameDao: GameDao
    ): GameRepository = GameRepositoryImpl(gameDao)

}