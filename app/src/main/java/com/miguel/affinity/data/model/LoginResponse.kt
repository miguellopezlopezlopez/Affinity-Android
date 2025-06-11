package com.miguel.affinity.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val user: UserData? = null,
    val redirect: String? = null
) {
    data class UserData(
        @SerializedName("ID")
        val id: Int,
        @SerializedName("User")
        val username: String,
        @SerializedName("Email")
        val email: String,
        @SerializedName("Nombre")
        val nombre: String = "",
        @SerializedName("Apellido")
        val apellido: String = "",
        @SerializedName("genero")
        val genero: String = "",
        @SerializedName("ubicacion")
        val ubicacion: String = "",
        @SerializedName("foto")
        val foto: String = ""
    )
}
