package com.avengers.nibobnebob.config


import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class BearerInterceptor @Inject constructor() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val response = chain.proceed(originalRequest)

        // API 통신중 특정코드 에러 발생 (accessToken 만료)
        if (response.code == 410) {

            var isRefreshed = false
            var accessToken = ""

//            runBlocking {
                // 로컬에 refreshToken이 있다면
//                sharedPreferences.getString(X_REFRESH_TOKEN, null)?.let { refresh ->
//                    Log.d(TAG, refresh)
//                    // refresh API 호출
//                    val result = Retrofit.Builder()
//                        .baseUrl(BASE_DEV_URL)
//                        .addConverterFactory(GsonConverterFactory.create())
//                        .build()
//                        .create(RefreshAPI::class.java).refreshToken(refresh)
//
//                    if (result.isSuccessful) {
//                        Log.d(TAG,"리프래시 성공")
//                        result.body()?.let { body ->
//                            Log.d(TAG,body.accessToken)
//                            // refresh 성공시 로컬에 저장
//                            sharedPreferences.edit()
//                                .putString(X_ACCESS_TOKEN, body.accessToken)
//                                .putString(X_REFRESH_TOKEN, body.refreshToken)
//                                .apply()
//
//                            isRefreshed = true
//                            accessToken = body.accessToken
//                        }
//                    }else{
//                        val error =
//                            Gson().fromJson(result.errorBody()?.string(), ErrorResponse::class.java)
//                        Log.d(TAG,error.message)
//                    }
//                }
//            }
//
//            if (isRefreshed) {
//
//                // 기존 API 재호출
//                val newRequest = originalRequest.newBuilder()
//                    .addHeader("Authorization", accessToken)
//                    .build()
//
//                return chain.proceed(newRequest)
//            }
        }

        // 해당 특정 에러코드가 그대로 내려간다면, 세션 만료 처리
        return response
    }
}