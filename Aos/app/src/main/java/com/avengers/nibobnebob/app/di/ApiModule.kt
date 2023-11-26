package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.remote.GlobalApi
import com.avengers.nibobnebob.data.remote.MyPageApi
import com.avengers.nibobnebob.data.remote.IntroApi
import com.avengers.nibobnebob.data.remote.RefreshApi
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
    fun provideIntroService(retrofit: Retrofit): IntroApi {
        return retrofit.create(IntroApi::class.java)
    }

    @Singleton
    @Provides
    fun provideValidationService(retrofit: Retrofit): ValidationApi {
        return retrofit.create(ValidationApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRefreshService(retrofit: Retrofit): RefreshApi {
        return retrofit.create(RefreshApi::class.java)
    }

    @Singleton
    @Provides
    fun provideGlobalService(retrofit: Retrofit) : GlobalApi {
        return retrofit.create(GlobalApi::class.java)
    }

    @Singleton
    @Provides
    fun provideMyPageService(retrofit: Retrofit) : MyPageApi = retrofit.create(MyPageApi::class.java)

}