package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.repository.MyPageRepository
import com.avengers.nibobnebob.data.repository_impl.MyPageRepositoryImpl
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

//    @Singleton
//    @Binds
//    abstract fun bindImageRepository(
//        imageRepositoryImpl: ImageRepositoryImpl
//    ): ImageRepository

}





