package com.avengers.data.model.mapper

import com.avengers.data.model.base.BaseDataModel
import com.avengers.nibobnebob.domain.model.base.BaseDomainModel

interface DomainMapper<in R : com.avengers.data.model.base.BaseDataModel, out D : BaseDomainModel> {
    fun R.toDomainModel() : D
}