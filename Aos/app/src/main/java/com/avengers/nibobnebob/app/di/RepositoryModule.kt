package com.avengers.nibobnebob.app.di

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

}





