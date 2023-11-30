package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.BaseState
import com.avengers.nibobnebob.data.model.response.BaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.UserDetailResponse
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.FollowApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val api: FollowApi
): FollowRepository {

    override fun getMyFollower(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>> = flow{
        val result = runRemote { api.getMyFollower() }
        emit(result)
    }

    override fun getMyFollowing(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>> = flow{
        val result = runRemote { api.getMyFollowing() }
        emit(result)
    }

    override fun getMyRecommendFollow(): Flow<BaseState<BaseResponse<List<FollowListResponse>>>> = flow{
        val result = runRemote { api.getMyRecommendFollow() }
        emit(result)
    }

    override fun follow(nickName: String): Flow<BaseState<BaseResponse<Unit>>> = flow{
        val result = runRemote { api.follow(nickName) }
        emit(result)
    }

    override fun unFollow(nickName: String): Flow<BaseState<BaseResponse<Unit>>> = flow{
        val result = runRemote { api.unFollow(nickName) }
        emit(result)
    }

    override fun searchFollow(keyword: String, region: List<String>): Flow<BaseState<BaseResponse<List<FollowListResponse>>>> = flow{
        val result = runRemote { api.searchFollow(keyword, region) }
        emit(result)
    }

    override fun getUserDetail(nick: String): Flow<BaseState<BaseResponse<UserDetailResponse>>> = flow {
        val result = runRemote { api.getUserDetail(nick) }
        emit(result)
    }
}