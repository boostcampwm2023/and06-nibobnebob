package com.avengers.nibobnebob.presentation.ui.intro.signup.mapper

import com.avengers.nibobnebob.data.model.response.DetailSignupResponse
import com.avengers.nibobnebob.presentation.ui.intro.signup.model.UiSignupData


internal fun DetailSignupResponse.toUiSignupData(): UiSignupData{
    return UiSignupData(
        testUi = this.testResponse
    )
}