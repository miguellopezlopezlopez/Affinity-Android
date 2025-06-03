package com.miguel.affinity.data.repository

import com.miguel.affinity.data.api.ApiService
import com.miguel.affinity.data.model.LoginRequest
import com.miguel.affinity.data.model.LoginResponse
import retrofit2.Call
import javax.inject.Inject

class AuthRepository @Inject constructor(private val apiService: ApiService) {
    fun login(user: String, password: String): Call<LoginResponse> {
        val loginRequest = LoginRequest(email = user, password = password)
        return apiService.login(loginRequest)
    }
}