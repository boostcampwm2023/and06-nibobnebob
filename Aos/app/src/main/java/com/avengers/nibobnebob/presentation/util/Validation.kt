package com.avengers.nibobnebob.presentation.util

object Validation {

    private val birthRegex = Regex("""^\d{4}/\d{2}/\d{2}${'$'}""")

    fun checkBirth(birth : String): Boolean{
        return birth.matches(birthRegex)
    }
}