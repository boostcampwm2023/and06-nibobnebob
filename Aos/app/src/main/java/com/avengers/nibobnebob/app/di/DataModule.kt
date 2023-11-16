package com.avengers.nibobnebob.app.di

import android.content.Context
import com.avengers.nibobnebob.app.App.Companion.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore
}