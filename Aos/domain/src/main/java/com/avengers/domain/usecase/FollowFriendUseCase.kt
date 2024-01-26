package com.avengers.nibobnebob.domain.usecase

import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.FollowRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FollowFriendUseCase @Inject constructor(
    private val repository: FollowRepository
) {
    operator fun invoke(nickName: String): Flow<BaseState<Unit>> = repository.follow(nickName)
}