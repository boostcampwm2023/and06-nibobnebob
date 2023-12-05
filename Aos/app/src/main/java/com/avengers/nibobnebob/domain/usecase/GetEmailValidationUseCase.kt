package com.avengers.nibobnebob.domain.usecase

import com.avengers.nibobnebob.domain.model.ValidateData
import com.avengers.nibobnebob.domain.model.base.BaseState
import com.avengers.nibobnebob.domain.repository.ValidationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmailValidationUseCase @Inject constructor(
    private val repository: ValidationRepository
) {
    operator fun invoke(email: String) : Flow<BaseState<ValidateData>> = repository.emailValidation(email)

}