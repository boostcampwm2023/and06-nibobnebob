package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.remote.IntroAPI
import com.avengers.nibobnebob.data.remote.ValidationApi
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
    fun provideIntroService(retrofit: Retrofit): IntroAPI {
        return retrofit.create(IntroAPI::class.java)
    }

    @Singleton
    @Provides
    fun provideValidationService(retrofit: Retrofit): ValidationApi {
        return retrofit.create(ValidationApi::class.java)
    }

}