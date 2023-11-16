package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.repository.IntroRepository
import com.avengers.nibobnebob.data.repository.IntroRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindImageRepository(
        introRepositoryImpl: IntroRepositoryImpl
    ): IntroRepository

}





