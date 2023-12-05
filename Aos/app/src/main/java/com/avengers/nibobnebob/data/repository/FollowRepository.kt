package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.UserDetailResponse
import kotlinx.coroutines.flow.Flow

interface FollowRepository {

    fun getMyFollower(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>>

    fun getMyFollowing(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>>

    fun getMyRecommendFollow(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>>

    fun follow(nickName: String): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun unFollow(nickName: String): Flow<OldBaseState<OldBaseResponse<Unit>>>

    fun searchFollow(keyword: String, region: List<String>): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>>

    fun getUserDetail(nick: String): Flow<OldBaseState<OldBaseResponse<UserDetailResponse>>>
}