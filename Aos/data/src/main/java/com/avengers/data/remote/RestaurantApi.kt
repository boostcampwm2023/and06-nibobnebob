package com.avengers.data.remote


import com.avengers.data.model.response.RecommendRestaurantResponse
import com.avengers.data.model.response.RestaurantDetailResponse
import com.avengers.data.model.response.RestaurantIsWishResponse
import com.avengers.data.model.response.RestaurantItemResponse
import com.avengers.data.model.response.ReviewSortResponse
import com.avengers.data.model.response.SearchRestaurantResponse
import com.avengers.data.model.response.WishRestaurantResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RestaurantApi {

    @GET("api/restaurant/{restaurantId}/details")
    suspend fun restaurantDetail(
        @Path("restaurantId") restaurantId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<RestaurantDetailResponse>>

    @GET("api/review/{restaurantId}")
    suspend fun sortReview(
        @Path("restaurantId") restaurantId: Int,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null
    ): Response<com.avengers.data.model.response.BaseResponse<ReviewSortResponse>>

    @Multipart
    @POST("api/user/restaurant/{restaurantId}")
    suspend fun addRestaurant(
        @Path("restaurantId") restaurantId: Int,
        @Part("isCarVisit") isCarVisit: Boolean,
        @Part("transportationAccessibility") transportationAccessibility: Int?,
        @Part("parkingArea") parkingArea: Int?,
        @Part("taste") taste: Int,
        @Part("service") service: Int,
        @Part("restroomCleanliness") restroomCleanliness: Int,
        @Part("overallExperience") overallExperience: RequestBody,
        @Part reviewImage: MultipartBody.Part
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @Multipart
    @POST("api/user/restaurant/{restaurantId}")
    suspend fun addRestaurantNoImage(
        @Path("restaurantId") restaurantId: Int,
        @Part("isCarVisit") isCarVisit: Boolean,
        @Part("transportationAccessibility") transportationAccessibility: Int?,
        @Part("parkingArea") parkingArea: Int?,
        @Part("taste") taste: Int,
        @Part("service") service: Int,
        @Part("restroomCleanliness") restroomCleanliness: Int,
        @Part("overallExperience") overallExperience: RequestBody,
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @DELETE("api/user/restaurant/{restaurantid}")
    suspend fun deleteRestaurant(
        @Path("restaurantid") restaurantId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    // 내 맛집 리스트
    @GET("api/user/restaurant")
    suspend fun myRestaurantList(
        @Query("longitude") longitude: String? = null,
        @Query("latitude") latitude: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null,
    ): Response<com.avengers.data.model.response.BaseResponse<com.avengers.data.model.response.MyRestaurantResponse>>

    // 내 위시 리스트
    @GET("api/user/wish-restaurant")
    suspend fun myWishList(
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("sort") sort: String? = null,
    ): Response<com.avengers.data.model.response.BaseResponse<WishRestaurantResponse>>

    @POST("api/user/wish-restaurant/{restaurantId}")
    suspend fun addWishRestaurant(
        @Path("restaurantId") restaurantId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @DELETE("api/user/wish-restaurant/{restaurantId}")
    suspend fun deleteWishRestaurant(
        @Path("restaurantId") restaurantId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @GET("api/user/state/wish-restaurant")
    suspend fun getRestaurantIsWish(
        @Query("restaurantid") id: Int
    ): Response<com.avengers.data.model.response.BaseResponse<RestaurantIsWishResponse>>


    @GET("api/restaurant/autocomplete/{partialRestaurantName}")
    suspend fun searchRestaurant(
        @Path("partialRestaurantName") name: String,
        @Query("radius") radius: String?,
        @Query("longitude") longitude: String?,
        @Query("latitude") latitude: String?
    ): Response<com.avengers.data.model.response.BaseResponse<List<SearchRestaurantResponse>>>

    //filter 맛집 리스트
    @GET("api/restaurant")
    suspend fun filterRestaurantList(
        @Query("filter") filter: String,
        @Query("location") location: String,
        @Query("radius") radius: Int
    ): Response<com.avengers.data.model.response.BaseResponse<List<RestaurantItemResponse>>>

    //위치기반 맛집 리스트
    @GET("api/restaurant/all")
    suspend fun nearRestaurantList(
        @Query("limit") limit : Int?,
        @Query("radius") radius: String,
        @Query("longitude") longitude: String,
        @Query("latitude") latitude: String
    ): Response<com.avengers.data.model.response.BaseResponse<List<RestaurantItemResponse>>>

    @POST("api/review/{reviewId}/like")
    suspend fun likeReview(
        @Path("reviewId") reviewId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @POST("api/review/{reviewId}/unlike")
    suspend fun unlikeReview(
        @Path("reviewId") reviewId: Int
    ): Response<com.avengers.data.model.response.BaseResponse<Unit>>

    @GET("api/user/recommend-food")
    suspend fun recommendRestaurantList()
            : Response<com.avengers.data.model.response.BaseResponse<List<RecommendRestaurantResponse>>>
}