package com.avengers.nibobnebob.data.model.mapper

import com.avengers.nibobnebob.data.model.base.BaseDataModel
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

interface DomainMapper<in R : BaseDataModel, out D : BaseDomainModel> {
    fun R.toDomainModel() : D
}