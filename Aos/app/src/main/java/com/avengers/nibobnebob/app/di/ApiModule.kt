package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.remote.NnApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Singleton
    @Provides
    fun provideNnApi(retrofit: Retrofit) : NnApi = retrofit.create(NnApi::class.java)

}