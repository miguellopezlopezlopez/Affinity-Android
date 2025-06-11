package com.miguel.affinity.data.api

import com.miguel.affinity.data.model.LoginRequest
import com.miguel.affinity.data.model.LoginResponse
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call
import retrofit2.http.Headers

interface ApiService {
    @POST("login.php")
    @Headers("Content-Type: application/json")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>
}