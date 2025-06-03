package com.miguel.affinity.data.model

data class LoginRequest(
    val email: String,  // This will match the PHP expectation
    val password: String
)