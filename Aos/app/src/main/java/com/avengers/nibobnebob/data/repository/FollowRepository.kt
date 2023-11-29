package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.FollowSearchResponse
import kotlinx.coroutines.flow.Flow

interface FollowRepository {

    fun getMyFollower(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>>

    fun getMyFollowing(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>>

    fun getMyRecommendFollow(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>>

    fun follow(nickName: String): Flow<BaseState<BaseResponse<Unit>>>

    fun unFollow(nickName: String): Flow<BaseState<BaseResponse<Unit>>>

    fun searchFollow(keyword: String, region: List<String>): Flow<BaseState<BaseResponse<FollowSearchResponse>>>
}