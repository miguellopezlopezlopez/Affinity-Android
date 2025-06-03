package com.miguel.affinity.data.api

import com.miguel.affinity.data.model.LoginRequest
import com.miguel.affinity.data.model.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface ApiService {
    @POST("login.php")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}