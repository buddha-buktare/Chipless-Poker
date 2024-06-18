package me.buddha.chiplesspoker.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.repository.GameRepository
import me.buddha.chiplesspoker.data.repository.GameRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ApplicationModule {

    @Binds
    @Singleton
    abstract fun bindGameRepository(
        repositoryImpl: GameRepositoryImpl
    ): GameRepository

}