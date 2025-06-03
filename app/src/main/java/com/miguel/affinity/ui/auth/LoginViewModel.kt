package com.miguel.affinity.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miguel.affinity.data.model.LoginResponse
import com.miguel.affinity.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.util.Log

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var loginResult by mutableStateOf<LoginResponse?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun login(user: String, password: String) {
        // Basic validation
        if (user.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            return
        }

        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            val call = authRepository.login(user, password)
            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    isLoading = false

                    if (response.isSuccessful) {
                        val body = response.body()
                        Log.d("LoginViewModel", "Response: $body")

                        if (body != null) {
                            loginResult = body
                            if (!body.success) {
                                errorMessage = body.message
                            }
                        } else {
                            errorMessage = "Respuesta vacía del servidor"
                        }
                    } else {
                        Log.e("LoginViewModel", "Error response: ${response.errorBody()?.string()}")
                        errorMessage = "Error del servidor: ${response.code()}"
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    isLoading = false
                    Log.e("LoginViewModel", "Network error", t)
                    errorMessage = "Error de conexión: ${t.message}"
                    loginResult = null
                }
            })
        }
    }

    fun clearError() {
        errorMessage = null
    }
}