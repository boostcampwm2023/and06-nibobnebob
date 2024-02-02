package com.avengers.data.model.response

import com.avengers.data.model.base.BaseDataModel
import com.avengers.data.model.mapper.DomainMapper
import com.avengers.nibobnebob.domain.model.ValidateData
import com.google.gson.annotations.SerializedName

data class ValidateResponse(
    @SerializedName("isexist")
    val isExist: Boolean
) : com.avengers.data.model.base.BaseDataModel {
    companion object : com.avengers.data.model.mapper.DomainMapper<ValidateResponse, ValidateData> {
        override fun ValidateResponse.toDomainModel(): ValidateData = ValidateData(
            isExist = isExist
        )
    }
}
