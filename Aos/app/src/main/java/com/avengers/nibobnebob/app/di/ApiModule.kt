package com.avengers.nibobnebob.app.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    // 예시
//    @Singleton
//    @Provides
//    fun provideIntroService(retrofit: Retrofit): IntroAPI {
//        return retrofit.create(IntroAPI::class.java)
//    }

}