package com.avengers.nibobnebob.config

import android.util.Log
import com.avengers.data.config.DataStoreManager
import com.avengers.data.model.Constants.AUTHORIZATION
import com.avengers.data.model.Constants.BEARER
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(private val dataStoreManager: DataStoreManager): Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val accessToken =  runBlocking {
            dataStoreManager.getAccessToken().first()
        }

        Log.d("token",accessToken.toString())
        val builder: Request.Builder = chain.request().newBuilder()
        accessToken?.takeIf { it.isNotEmpty() }?.let {
            builder.addHeader(AUTHORIZATION, "$BEARER $it")
        }
        return chain.proceed(builder.build())
    }
}