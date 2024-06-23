package me.buddha.chiplesspoker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.local.GameDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGameDatabase(
        @ApplicationContext appContext: Context
    ): GameDatabase = Room.databaseBuilder(
        context = appContext,
        GameDatabase::class.java,
        "GameDB"
    ).build()

    @Provides
    @Singleton
    fun provideGameDao(
        gameDatabase: GameDatabase
    ) = gameDatabase.gameDao()
}