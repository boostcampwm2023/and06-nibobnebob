package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.remote.NibobNebobApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    fun provideNibobNebobService(retrofit: Retrofit) : NibobNebobApi {
        return retrofit.create(NibobNebobApi::class.java)
    }

}