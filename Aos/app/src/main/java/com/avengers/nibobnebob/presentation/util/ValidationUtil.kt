package com.avengers.nibobnebob.presentation.util

object ValidationUtil {

    private val birthRegex = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")
    private val emailRegex = Regex(".+@.+\\.\\D+")

    fun checkBirth(birth: String): Boolean {
        return birth.matches(birthRegex)
    }

    fun checkEmail(email: String): Boolean {
        return email.matches(emailRegex)
    }
}