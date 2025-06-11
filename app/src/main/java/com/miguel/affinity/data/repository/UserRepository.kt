package com.miguel.affinity.data.repository

import com.miguel.affinity.data.api.ApiService
import com.miguel.affinity.data.model.UserProfile
import com.miguel.affinity.data.model.LoginResponse
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val apiService: ApiService
) {
    // Obtener el perfil de usuario desde las preferencias compartidas
    suspend fun getUserProfile(username: String): UserProfile? {
        // Por ahora retornamos datos simulados
        // TODO: Implementar llamada real a la API
        return UserProfile(
            id = 1,
            user = username,
            email = "$username@example.com", // Esto debería venir de la API
            nombre = "Nombre",
            apellido = "Apellido",
            genero = "Otro",
            ubicacion = "Ubicación",
            foto = ""
        )
    }

    // Actualizar el perfil de usuario
    suspend fun updateUserProfile(profile: UserProfile, password: String): String {
        // TODO: Implementar llamada real a la API
        return "Datos actualizados correctamente."
    }

    // Eliminar usuario
    suspend fun deleteUser(userId: Int): String {
        // TODO: Implementar llamada real a la API
        return "Cuenta eliminada correctamente."
    }

    // Convertir UserData a UserProfile
    fun convertUserDataToProfile(userData: LoginResponse.UserData): UserProfile {
        return UserProfile(
            id = userData.id,
            user = userData.username,
            email = userData.email,
            nombre = userData.nombre,
            apellido = userData.apellido,
            genero = userData.genero,
            ubicacion = userData.ubicacion,
            foto = userData.foto
        )
    }
}