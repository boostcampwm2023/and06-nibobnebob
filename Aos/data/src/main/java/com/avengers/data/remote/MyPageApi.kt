package com.avengers.data.remote

import com.avengers.data.model.response.BaseResponse
import com.avengers.data.model.response.MyDefaultInfoResponse
import com.avengers.data.model.response.MyInfoResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part

interface MyPageApi {
    @GET("api/user/details")
    suspend fun getMyInfo(): Response<com.avengers.data.model.response.BaseResponse<com.avengers.data.model.response.MyInfoResponse>>

    @GET("api/user")
    suspend fun getMyDefaultInfo(): Response<com.avengers.data.model.response.BaseResponse<com.avengers.data.model.response.MyDefaultInfoResponse>>

    @Multipart
    @PUT("api/user")
    suspend fun editInfo(
        @Part("email") email: RequestBody,
        @Part("provider") provider: RequestBody,
        @Part("nickName") nickName: RequestBody,
        @Part("region") region: RequestBody,
        @Part("birthdate") birthdate: RequestBody,
        @Part("isMale") isMale: Boolean,
        @Part("isImageChanged") isImageChanged: Boolean,
        @Part profileImage: MultipartBody.Part?,
        @Part("profileImage") profileImageString: RequestBody?
    ): Response<Unit>


    @POST("api/user/logout")
    suspend fun logout(): Response<Unit>

    @DELETE("api/user")
    suspend fun withdraw(): Response<Unit>

}