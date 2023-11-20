package com.avengers.nibobnebob.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

interface ValidationApi {

    @GET("/api/user/nickname/{nickname}/exists")
    fun nickValidation(
        @Path("nickname") nickname: String
    ): Unit

    @GET("/api/user/email/{email}/exists")
    fun emailValidation(
        @Path("email") email: String
    ): Unit
}