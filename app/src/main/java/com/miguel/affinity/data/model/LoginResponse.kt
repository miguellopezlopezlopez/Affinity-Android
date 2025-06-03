package com.miguel.affinity.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: UserData? = null,
    val redirect: String? = null
)

data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    @SerializedName("nombre_completo")
    val nombreCompleto: String
)
