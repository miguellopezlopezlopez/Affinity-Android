package com.miguel.affinity.data.model

data class UserProfile(
    val id: Int,
    val user: String,
    val email: String,
    val nombre: String,
    val apellido: String,
    val genero: String,
    val ubicacion: String,
    val foto: String // URL o ruta de la foto
)