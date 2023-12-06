package com.avengers.nibobnebob.domain.repository

import com.avengers.nibobnebob.domain.model.MyDefaultInfoData
import com.avengers.nibobnebob.domain.model.MyInfoData
import com.avengers.nibobnebob.domain.model.base.BaseState
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MyPageRepository {
    fun getMyInfo(): Flow<BaseState<MyInfoData>>

    fun getMyDefaultInfo(): Flow<BaseState<MyDefaultInfoData>>

    fun editMyInfo(
        nickName: RequestBody,
        email: RequestBody,
        provider: RequestBody,
        birthdate: RequestBody,
        region: RequestBody,
        isMale: Boolean,
        password: RequestBody?,
        profileImage: MultipartBody.Part?
    ): Flow<BaseState<Unit>>

    fun editMyInfoNoImage(
        nickName : String,
        email : String,
        provider : String,
        birthdate : String,
        region : String,
        isMale : Boolean
    ) : Flow<BaseState<Unit>>


    fun logout(): Flow<BaseState<Unit>>

    fun withdraw(): Flow<BaseState<Unit>>

}