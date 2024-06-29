package me.buddha.chiplesspoker.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.data.local.TableDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTableDatabase(
        @ApplicationContext appContext: Context
    ): TableDatabase = Room.databaseBuilder(
        context = appContext,
        TableDatabase::class.java,
        "TableDB"
    ).build()

    @Provides
    @Singleton
    fun provideTableDao(
        tableDatabase: TableDatabase
    ) = tableDatabase.tableDao()
}