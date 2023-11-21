package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.repository.LoginRepository
import com.avengers.nibobnebob.data.repository.LoginRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

//    @Singleton
//    @Binds
//    abstract fun bindImageRepository(
//        imageRepositoryImpl: ImageRepositoryImpl
//    ): ImageRepository

    @Singleton
    @Binds
    abstract fun bindLoginRepository(
        loginRepositoryImpl : LoginRepositoryImpl
    ) : LoginRepository

}





