package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.response.FollowListResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.response.UserDetailResponse.Companion.toDomainModel
import com.avengers.nibobnebob.data.model.runRemote
import com.avengers.nibobnebob.data.remote.FollowApi
import com.avengers.nibobnebob.domain.model.FollowListData
import com.avengers.nibobnebob.domain.model.UserDetailData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.model.base.StatusCode
import com.avengers.nibobnebob.domain.repository.FollowRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FollowRepositoryImpl @Inject constructor(
    private val api: FollowApi
) : FollowRepository {

    override fun getMyFollower(): Flow<BaseState<List<FollowListData>>> = flow {

        when (val result = runRemote { api.getMyFollower() }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getMyFollowing(): Flow<BaseState<List<FollowListData>>> = flow {

        when (val result = runRemote { api.getMyFollowing() }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getMyRecommendFollow(): Flow<BaseState<List<FollowListData>>> = flow {

        when (val result = runRemote { api.getMyRecommendFollow() }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun follow(nickName: String): Flow<BaseState<Unit>> = flow {

        when (val result = runRemote { api.follow(nickName) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body))
                } ?: run {
                    emit(BaseState.Success(Unit))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun unFollow(nickName: String): Flow<BaseState<Unit>> = flow {

        when (val result = runRemote { api.unFollow(nickName) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body))
                } ?: run {
                    emit(BaseState.Success(Unit))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun searchFollow(
        keyword: String,
        region: List<String>
    ): Flow<BaseState<List<FollowListData>>> = flow {

        when (val result = runRemote { api.searchFollow(keyword, region) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.map { it.toDomainModel() }))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }

    override fun getUserDetail(nick: String): Flow<BaseState<UserDetailData>> = flow {

        when (val result = runRemote { api.getUserDetail(nick) }) {
            is BaseState.Success -> {
                result.data.body?.let { body ->
                    emit(BaseState.Success(body.toDomainModel()))
                } ?: run {
                    emit(BaseState.Error(StatusCode.EMPTY, "null 수신"))
                }
            }

            is BaseState.Error -> {
                emit(result)
            }
        }
    }
}