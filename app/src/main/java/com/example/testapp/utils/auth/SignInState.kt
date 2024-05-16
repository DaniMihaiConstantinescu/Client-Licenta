package com.example.testapp.utils.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
