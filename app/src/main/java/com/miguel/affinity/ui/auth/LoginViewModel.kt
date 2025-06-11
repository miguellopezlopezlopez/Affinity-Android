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
import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var loginResult by mutableStateOf<LoginResponse?>(null)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getContext(): Context = context

    fun setError(message: String) {
        errorMessage = message
    }

    fun login(user: String, password: String) {
        Log.d("LoginViewModel", "Starting login process for user: $user")
        
        if (isLoading) {
            Log.d("LoginViewModel", "Login already in progress, ignoring request")
            return
        }
        
        // Reset states
        loginResult = null
        errorMessage = null
        
        // Validación básica
        if (user.isBlank() || password.isBlank()) {
            errorMessage = "Por favor, completa todos los campos"
            Log.d("LoginViewModel", "Validation failed: empty fields")
            return
        }

        isLoading = true
        Log.d("LoginViewModel", "Starting API call")

        viewModelScope.launch {
            try {
                val call = authRepository.login(user, password)
                Log.d("LoginViewModel", "API call created")
                
                call.enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        Log.d("LoginViewModel", "Got response, code: ${response.code()}")
                        
                        if (response.isSuccessful) {
                            val body = response.body()
                            Log.d("LoginViewModel", "Response body: $body")

                            if (body != null) {
                                if (body.success && body.user != null) {
                                    val username = body.user.username
                                    Log.d("LoginViewModel", "Login successful with user: $username")
                                    // Verificar que el username no sea null ni vacío
                                    if (!username.isNullOrBlank()) {
                                        loginResult = body
                                        errorMessage = null
                                    } else {
                                        Log.e("LoginViewModel", "Username is null or blank")
                                        errorMessage = "Error: Datos de usuario inválidos"
                                        loginResult = null
                                    }
                                } else {
                                    Log.d("LoginViewModel", "Login failed with message: ${body.message}")
                                    errorMessage = body.message ?: "Error desconocido"
                                    loginResult = null
                                }
                            } else {
                                Log.e("LoginViewModel", "Response body is null")
                                errorMessage = "Error: Respuesta vacía del servidor"
                                loginResult = null
                            }
                        } else {
                            handleErrorResponse(response)
                        }
                        isLoading = false
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Log.e("LoginViewModel", "Network error", t)
                        Log.e("LoginViewModel", "Error message: ${t.message}")
                        errorMessage = "Error de conexión: ${t.message}"
                        loginResult = null
                        isLoading = false
                    }
                })
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Exception during login", e)
                errorMessage = "Error inesperado: ${e.message}"
                loginResult = null
                isLoading = false
            }
        }
    }

    private fun handleErrorResponse(response: Response<LoginResponse>) {
        val errorBody = response.errorBody()?.string()
        Log.e("LoginViewModel", "Error response code: ${response.code()}")
        Log.e("LoginViewModel", "Error body: $errorBody")
        
        errorMessage = try {
            val gson = com.google.gson.Gson()
            val errorResponse = gson.fromJson(errorBody, LoginResponse::class.java)
            errorResponse?.message ?: "Error del servidor: ${response.code()}"
        } catch (e: Exception) {
            Log.e("LoginViewModel", "Error parsing error response", e)
            "Error del servidor: ${response.code()}"
        }
        loginResult = null
    }

    fun clearError() {
        Log.d("LoginViewModel", "Clearing error")
        errorMessage = null
    }

    fun clearStates() {
        Log.d("LoginViewModel", "Clearing all states")
        errorMessage = null
        loginResult = null
        isLoading = false
    }
}
