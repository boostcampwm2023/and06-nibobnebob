package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.repository.IntroRepository
import com.avengers.nibobnebob.data.repository.IntroRepositoryImpl
import com.avengers.nibobnebob.data.repository.MyPageRepository
import com.avengers.nibobnebob.data.repository.ValidationRepository
import com.avengers.nibobnebob.data.repository.ValidationRepositoryImpl
import com.avengers.nibobnebob.data.repository.MyPageRepositoryImpl
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
    abstract fun bindMyPageRepository(myPageRepositoryImpl: MyPageRepositoryImpl): MyPageRepository

    @Singleton
    @Binds
    abstract fun bindIntroRepository(
        introRepositoryImpl: IntroRepositoryImpl
    ): IntroRepository

    @Singleton
    @Binds
    abstract fun bindValidationRepository(
        validationRepositoryImpl: ValidationRepositoryImpl
    ): ValidationRepository


}





