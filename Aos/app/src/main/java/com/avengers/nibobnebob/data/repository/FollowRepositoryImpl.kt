package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.OldBaseState
import com.avengers.nibobnebob.data.model.response.OldBaseResponse
import com.avengers.nibobnebob.data.model.response.FollowListResponse
import com.avengers.nibobnebob.data.model.response.UserDetailResponse
import com.avengers.nibobnebob.data.model.oldRunRemote
import com.avengers.nibobnebob.data.remote.FollowApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val api: FollowApi
): FollowRepository {

    override fun getMyFollower(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>> = flow{
        val result = oldRunRemote { api.getMyFollower() }
        emit(result)
    }

    override fun getMyFollowing(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>> = flow{
        val result = oldRunRemote { api.getMyFollowing() }
        emit(result)
    }

    override fun getMyRecommendFollow(): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>> = flow{
        val result = oldRunRemote { api.getMyRecommendFollow() }
        emit(result)
    }

    override fun follow(nickName: String): Flow<OldBaseState<OldBaseResponse<Unit>>> = flow{
        val result = oldRunRemote { api.follow(nickName) }
        emit(result)
    }

    override fun unFollow(nickName: String): Flow<OldBaseState<OldBaseResponse<Unit>>> = flow{
        val result = oldRunRemote { api.unFollow(nickName) }
        emit(result)
    }

    override fun searchFollow(keyword: String, region: List<String>): Flow<OldBaseState<OldBaseResponse<List<FollowListResponse>>>> = flow{
        val result = oldRunRemote { api.searchFollow(keyword, region) }
        emit(result)
    }

    override fun getUserDetail(nick: String): Flow<OldBaseState<OldBaseResponse<UserDetailResponse>>> = flow {
        val result = oldRunRemote { api.getUserDetail(nick) }
        emit(result)
    }
}