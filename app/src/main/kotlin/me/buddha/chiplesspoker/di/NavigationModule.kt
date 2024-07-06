package me.buddha.chiplesspoker.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import me.buddha.chiplesspoker.domain.navigation.NavigationService

@Module
@InstallIn(ActivityComponent::class)
object NavigationModule {

    @Provides
    fun provideNavController(@ApplicationContext context: Context) =
        NavigationService(context).navController
}