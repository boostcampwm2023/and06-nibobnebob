package com.avengers.nibobnebob.config

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.avengers.nibobnebob.presentation.util.Constants.ACCESS
import com.avengers.nibobnebob.presentation.util.Constants.ACCESSTOKEN
import com.avengers.nibobnebob.presentation.util.Constants.AUTHORIZATION
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(private val dataStore : DataStore<Preferences>): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
            val key = stringPreferencesKey(ACCESSTOKEN)

            val accessToken =  runBlocking {
                val preferences = dataStore.data.first()
                preferences[key]
            }
        val builder: Request.Builder = chain.request().newBuilder()
        accessToken?.let {
            builder.addHeader(AUTHORIZATION,"$ACCESS $accessToken")
        }
        return chain.proceed(builder.build())
    }
}