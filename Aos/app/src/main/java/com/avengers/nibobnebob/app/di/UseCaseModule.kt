package com.avengers.nibobnebob.app.di

import com.avengers.nibobnebob.domain.repository.HomeRepository
import com.avengers.nibobnebob.domain.usecase.GetFilterRestaurantListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {
    @Provides
    @ViewModelScoped
    fun provideGetFilterRestaurantListUseCase(repository: HomeRepository): GetFilterRestaurantListUseCase {
        return GetFilterRestaurantListUseCase(repository)
    }
}