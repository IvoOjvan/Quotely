package com.example.qoutely.model

data class LoginModel(
    var email: String = "",
    val emailError: String? = null,
    var password: String = "",
    val passwordError: String? = null,
    var passwordVisibility: Boolean = false,
    var loginError: Boolean = false,
    val loginErrorMessage: String? = null
)
