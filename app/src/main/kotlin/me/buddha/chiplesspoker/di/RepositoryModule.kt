package me.buddha.chiplesspoker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.local.TableDao
import me.buddha.chiplesspoker.data.repository.TableRepository
import me.buddha.chiplesspoker.data.repository.TableRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideTableRepository(
        tableDao: TableDao
    ): TableRepository = TableRepositoryImpl(tableDao)

}