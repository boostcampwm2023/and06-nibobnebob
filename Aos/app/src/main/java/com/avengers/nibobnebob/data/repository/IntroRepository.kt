package com.avengers.nibobnebob.data.repository

import com.avengers.nibobnebob.data.model.request.DetailSignupRequest
import com.avengers.nibobnebob.data.model.response.DetailSignupResponse
import kotlinx.coroutines.flow.Flow

interface IntroRepository {

    fun signup(
        body: DetailSignupRequest
    ): Flow<DetailSignupResponse>
}