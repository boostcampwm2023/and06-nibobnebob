package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.data.model.response.Location.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.Reviews.Companion.toDomainModel
import com.avengers.nibobnebob.domain.model.RestaurantDetailData
import com.avengers.nibobnebob.domain.model.ReviewSortData
import com.avengers.nibobnebob.domain.model.ReviewsData
import com.google.gson.annotations.SerializedName

data class RestaurantDetailResponse(
    @SerializedName("isMy") val isMy: Boolean,
    @SerializedName("isWish") val isWish: Boolean,
    @SerializedName("restaurant_address") val address: String,
    @SerializedName("restaurant_category") val category: String,
    @SerializedName("restaurant_id") val id: Int,
    @SerializedName("restaurant_location") val location: Location,
    @SerializedName("restaurant_name") val name: String,
    @SerializedName("restaurant_phoneNumber") val phoneNumber: String,
    @SerializedName("restaurant_reviewCnt") val reviewCnt: Int,
    @SerializedName("reviews") val reviews: List<Reviews>?
) : BaseDataModel {
    companion object : DomainMapper<RestaurantDetailResponse, RestaurantDetailData> {
        override fun RestaurantDetailResponse.toDomainModel(): RestaurantDetailData =
            RestaurantDetailData(
                isMy = isMy,
                isWish = isWish,
                address = address,
                category = category,
                id = id,
                location = location.toDomainModel(),
                name = name,
                phoneNumber = phoneNumber,
                reviewCnt = reviewCnt,
                reviews = reviews?.map { it.toDomainModel() }
            )
    }
}

data class ReviewSortResponse(
    @SerializedName("hasNext") val hasNext: Boolean,
    @SerializedName("items") val reviewItems: List<Reviews>
) : BaseDataModel {
    companion object : DomainMapper<ReviewSortResponse, ReviewSortData> {
        override fun ReviewSortResponse.toDomainModel(): ReviewSortData = ReviewSortData(
            hasNext = hasNext,
            reviewItems = reviewItems.map { it.toDomainModel() }
        )
    }
}

data class Reviews(
    @SerializedName("review_id") val id: Int,
    @SerializedName("review_created_at") val createdAt: String,
    @SerializedName("review_isCarVisit") val isCarVisit: Boolean,
    @SerializedName("review_overallExperience") val overallExperience: String,
    @SerializedName("review_parkingArea") val parkingArea: Int,
    @SerializedName("review_restroomCleanliness") val restroomCleanliness: Int,
    @SerializedName("review_service") val service: Int,
    @SerializedName("review_taste") val taste: Int,
    @SerializedName("review_transportationAccessibility") val transportationAccessibility: Int,
    @SerializedName("reviewer") val reviewer: String,
    @SerializedName("review_reviewImage") val reviewImage: String,
    @SerializedName("user_profileImage") val userProfileImage: String,
    @SerializedName("islike") val isLike: Boolean?,
    @SerializedName("likeCount") val likeCount: Int,
    @SerializedName("dislikeCount") val dislikeCount: Int,
) : BaseDataModel {
    companion object : DomainMapper<Reviews, ReviewsData> {
        override fun Reviews.toDomainModel(): ReviewsData = ReviewsData(
            id = id,
            createdAt = createdAt,
            isCarVisit = isCarVisit,
            overallExperience = overallExperience,
            parkingArea = parkingArea,
            restroomCleanliness = restroomCleanliness,
            service = service,
            taste = taste,
            transportationAccessibility = transportationAccessibility,
            reviewer = reviewer,
            reviewImage = reviewImage,
            userProfileImage = userProfileImage,
            isLike = isLike,
            likeCount = likeCount,
            dislikeCount = dislikeCount
        )
    }
}