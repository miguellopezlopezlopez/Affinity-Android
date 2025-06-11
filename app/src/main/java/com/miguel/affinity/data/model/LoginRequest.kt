package com.miguel.affinity.data.model

data class LoginRequest(
    val user: String,  // Campo que coincide con el backend
    val password: String
)