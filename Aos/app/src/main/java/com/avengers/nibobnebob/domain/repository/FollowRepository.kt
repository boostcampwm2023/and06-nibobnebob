package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.FollowListData
import com.avengers.nibobnebob.domain.model.RecommendFollowListData
import com.avengers.nibobnebob.domain.model.UserDetailData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface FollowRepository {
    fun getMyFollower(): Flow<BaseState<List<FollowListData>>>

    fun getMyFollowing(): Flow<BaseState<List<FollowListData>>>

    fun getMyRecommendFollow(): Flow<BaseState<List<RecommendFollowListData>>>

    fun follow(nickName: String): Flow<BaseState<Unit>>

    fun unFollow(nickName: String): Flow<BaseState<Unit>>

    fun searchFollow(keyword: String, region: List<String>): Flow<BaseState<List<FollowListData>>>

    fun getUserDetail(nick: String): Flow<BaseState<UserDetailData>>
}