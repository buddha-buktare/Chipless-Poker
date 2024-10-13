package me.buddha.chiplesspoker.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.buddha.chiplesspoker.domain.navigation.DefaultNavigator
import me.buddha.chiplesspoker.domain.navigation.Destination
import me.buddha.chiplesspoker.domain.navigation.Navigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNavigator(): Navigator =
        DefaultNavigator(startDestination = Destination.Home)
}