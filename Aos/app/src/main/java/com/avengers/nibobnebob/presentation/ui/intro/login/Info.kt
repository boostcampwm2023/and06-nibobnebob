package com.avengers.nibobnebob.presentation.ui.intro.login

data class CommonRequest(
    val email : String ="",
    val password : String =""
)

data class NaverRequest(
    val token : String = "",
    val image : String = ""
)

data class LoginResponse(
    val accessToken : String = "",
    val refreshToken : String = ""
)