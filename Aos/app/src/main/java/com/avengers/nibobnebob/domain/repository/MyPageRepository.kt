package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.domain.model.MyInfoData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow

interface MyPageRepository {
    fun getMyInfo(): Flow<BaseState<MyInfoData>>

    fun getMyDefaultInfo(): Flow<BaseState<MyDefaultInfoData>>

    fun editMyInfo(
        nickName: String,
        email: String,
        provider: String,
        birthdate: String,
        region: String,
        isMale: Boolean,
        password: String,
        profileImage: String
    ): Flow<BaseState<Unit>>

    fun logout(): Flow<BaseState<Unit>>

    fun withdraw(): Flow<BaseState<Unit>>

}