package com.miguel.affinity.data.repository

import com.miguel.affinity.data.api.ApiService
import com.miguel.affinity.data.model.LoginRequest
import com.miguel.affinity.data.model.LoginResponse
import retrofit2.Call
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {

    fun login(user: String, password: String): Call<LoginResponse> {
        try {
            // Log de la solicitud
            android.util.Log.d("AuthRepository", "Creating login request - user: $user")
            
            val loginRequest = LoginRequest(user = user, password = password)
            android.util.Log.d("AuthRepository", "Login request created")
            
            return apiService.login(loginRequest)
        } catch (e: Exception) {
            android.util.Log.e("AuthRepository", "Error during login request creation", e)
            throw e
        }
    }
}
