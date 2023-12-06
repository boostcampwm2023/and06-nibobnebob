package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.data.repository.FollowRepositoryImpl
import com.avengers.nibobnebob.data.repository.HomeRepository
import com.avengers.nibobnebob.data.repository.HomeRepositoryImpl
import com.avengers.nibobnebob.data.repository.IntroRepositoryImpl
import com.avengers.nibobnebob.data.repository.MyPageRepositoryImpl
import com.avengers.nibobnebob.data.repository.RestaurantRepositoryImpl
import com.avengers.nibobnebob.data.repository.ValidationRepositoryImpl
import com.avengers.nibobnebob.data.repository.HomeRepository
import com.avengers.nibobnebob.domain.repository.FollowRepository
import com.avengers.nibobnebob.domain.repository.RestaurantRepository
import com.avengers.nibobnebob.domain.repository.IntroRepository
import com.avengers.nibobnebob.domain.repository.MyPageRepository
import com.avengers.nibobnebob.domain.repository.ValidationRepository
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

    @Singleton
    @Binds
    abstract fun bindRestaurantRepository(
        restaurantRepositoryImpl: RestaurantRepositoryImpl
    ): RestaurantRepository

    @Singleton
    @Binds
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Singleton
    @Binds
    abstract fun bindFollowRepository(
        followRepositoryImpl: FollowRepositoryImpl
    ): FollowRepository

}