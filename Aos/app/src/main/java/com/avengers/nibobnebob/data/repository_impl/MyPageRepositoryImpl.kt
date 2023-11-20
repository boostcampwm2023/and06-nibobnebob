package com.avengers.nibobnebob.data.repository_impl

import com.avengers.nibobnebob.data.model.ApiState
import com.avengers.nibobnebob.data.model.response.MyPageInfoResponse
import com.avengers.nibobnebob.data.remote.NnApi
import com.avengers.nibobnebob.data.repository.MyPageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MyPageRepositoryImpl @Inject constructor(private val nnApi: NnApi) : MyPageRepository {
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwiaWQiOjIsImlhdCI6MjAxNjIzOTAyMn0.VMvtPcawKVzNyOV08lrArxUvM-XxowWIknsPFkjRTws"
    override fun getMyPageInfo(): Flow<ApiState<MyPageInfoResponse>> = flow {
        try {
            val response = nnApi.getMyPageInfo(token)
            if(response.isSuccessful){
                emit(ApiState.Success(response.body()!!))
            }else{
                emit(ApiState.Error(response.code(), response.message()))
            }
        }catch (e: Exception) {
            emit(ApiState.Exception(e))
        }

    }



//    override fun sendSignUp(data: SignUpRequest): Flow<MyPageResponse?> = flow {
//        val response = nnApi.sendSignUp(data)
//        if(response.isSuccessful){
//            emit(nnApi.sendSignUp(data).body())
//        }else{
//            // val errorData = Gson().fromJson(response.errorBody()?.string(), ErrorResponse::class.java)
//            // 여기서 앱 안죽고 처리되도록 고민하기
//            response.errorBody()
//        }
//    }


//    override suspend fun sendSignUp(data: SignUpRequest): Flow<Boolean> {
//        runCatching {
//            nnApi.sendSignUp(data)
//        }.onSuccess{
//            return flow { true }
//        }.onFailure {
//            return flow { false }
//        }
//        return flow { true }
//    }

}