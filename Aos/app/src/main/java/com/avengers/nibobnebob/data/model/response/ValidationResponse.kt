package com.avengers.nibobnebob.data.model.response

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.ValidateData
import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("isexist")
    val isExist: Boolean
) : BaseDataModel {
    companion object : DomainMapper<ValidateResponse, ValidateData> {
        override fun ValidateResponse.toDomainModel(): ValidateData = ValidateData(
            isExist = isExist
        )
    }
}
