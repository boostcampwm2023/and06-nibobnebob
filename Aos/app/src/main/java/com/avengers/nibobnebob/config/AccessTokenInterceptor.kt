package com.avengers.nibobnebob.config

import android.util.Log
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.presentation.util.Constants.ACCESS
import com.avengers.nibobnebob.presentation.util.Constants.AUTHORIZATION
import com.avengers.nibobnebob.presentation.util.Constants.BEARER
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

        // 동기가 아닌 비동기로 불러와야한다.. runBlocking말고 다른 방안에 대해 고민
        val accessToken =  runBlocking {
            dataStoreManager.getAccessToken().first()
        }
        Log.d("토큰 테스트",accessToken.toString())
        val builder: Request.Builder = chain.request().newBuilder()
        accessToken?.takeIf { it.isNotEmpty() }?.let {
            builder.addHeader(AUTHORIZATION, "$BEARER $it")
        }
        return chain.proceed(builder.build())
    }
}