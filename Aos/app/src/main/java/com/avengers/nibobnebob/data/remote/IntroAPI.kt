package com.avengers.nibobnebob.data.remote

import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface IntroAPI {

    @POST("/api/user")
    suspend fun signup(
        @Body params: DetailSignupRequest
    ): Unit

}