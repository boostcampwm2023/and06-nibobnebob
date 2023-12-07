package com.avengers.nibobnebob.config


import android.content.Intent
import com.avengers.nibobnebob.app.App
import com.avengers.nibobnebob.app.DataStoreManager
import com.avengers.nibobnebob.data.model.request.RefreshTokenRequest
import com.avengers.nibobnebob.data.model.response.LoginResponse
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.RefreshApi
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.presentation.ui.intro.IntroActivity
import com.avengers.nibobnebob.presentation.util.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import javax.inject.Inject

class BearerInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        var newAccessToken: String? = null

        if (response.code == TOKEN_ERROR) {
            runBlocking {
                val refreshToken = dataStoreManager.getRefreshToken().first()
                refreshToken?.let { token ->
                    when (val result = getNewAccessToken(token)) {
                        is BaseState.Success -> {
                            response.close()
                            newAccessToken = result.data.body?.accessToken
                            newAccessToken?.let {
                                dataStoreManager.putAccessToken(newAccessToken!!)
                            }
                        }

                        else -> {
                            dataStoreManager.deleteAccessToken()
                            dataStoreManager.deleteRefreshToken()

                            val intent = Intent(App.getContext(), IntroActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            App.getContext().startActivity(intent)
                        }
                    }
                }
            }
            newAccessToken?.let {
                val newRequest = originalRequest.newBuilder()
                    .addHeader(AUTHORIZATION, "$BEARER $newAccessToken")
                    .build()
                return chain.proceed(newRequest)
            }
        }

        return response
    }


    private suspend fun getNewAccessToken(refreshToken: String?): BaseState<BaseResponse<LoginResponse>> {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val api = retrofit.create(RefreshApi::class.java)
        return runRemote { api.refreshAccessToken(RefreshTokenRequest(refreshToken)) }
    }

    companion object {
        const val TOKEN_ERROR = 401
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }
}


