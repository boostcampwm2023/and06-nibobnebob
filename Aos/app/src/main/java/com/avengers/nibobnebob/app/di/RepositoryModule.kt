package com.avengers.nibobnebob.app.di

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

    @Singleton
    @Binds
    abstract fun bindValidationRepository(
        validationRepositoryImpl: ValidationRepositoryImpl
    ): ValidationRepository

    @Singleton
    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryImpl : LoginRepositoryImpl
    ) : LoginRepository

}





