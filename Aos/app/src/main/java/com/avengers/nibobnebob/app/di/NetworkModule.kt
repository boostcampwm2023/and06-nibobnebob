package com.avengers.nibobnebob.app.di

import com.avengers.data.config.DataStoreManager
import com.avengers.nibobnebob.BuildConfig
import com.avengers.nibobnebob.config.AccessTokenInterceptor
import com.avengers.nibobnebob.config.BearerInterceptor
import com.avengers.nibobnebob.presentation.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun provideOkHttpClient(
        httpLoggingInterceptor: HttpLoggingInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
        bearerInterceptor: BearerInterceptor
    ): OkHttpClient {

        return OkHttpClient.Builder()
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .connectTimeout(10000, TimeUnit.MILLISECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(accessTokenInterceptor)
            .addInterceptor(bearerInterceptor)
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    fun provideAccessTokenInterceptor(dataStoreManager: DataStoreManager): AccessTokenInterceptor =
        AccessTokenInterceptor(dataStoreManager)

    @Provides
    fun provideBearerInterceptor(dataStoreManager: DataStoreManager): BearerInterceptor =
        BearerInterceptor(dataStoreManager)

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}