package com.miguel.affinity.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import kotlinx.coroutines.delay

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: (String) -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    android.util.Log.d("LoginScreen", "Composing LoginScreen")
    
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showValidationError by remember { mutableStateOf(false) }
    var isNavigating by remember { mutableStateOf(false) }

    // Limpiar estados al montar el componente
    LaunchedEffect(Unit) {
        android.util.Log.d("LoginScreen", "Initial LaunchedEffect - clearing states")
        viewModel.clearStates()
    }

    // Observar el resultado del login
    LaunchedEffect(viewModel.loginResult) {
        viewModel.loginResult?.let { result ->
            android.util.Log.d("LoginScreen", "Login result changed: success=${result.success}, user=${result.user}")
            if (!isNavigating && result.success && result.user != null && !viewModel.isLoading) {
                val username = result.user.username
                android.util.Log.d("LoginScreen", "Preparing to navigate to profile with username: $username")
                if (username.isNotBlank()) {
                    try {
                        isNavigating = true
                        android.util.Log.d("LoginScreen", "Starting navigation delay")
                        // Pequeña pausa para asegurar que la UI se actualice
                        kotlinx.coroutines.delay(800)
                        // Verificar que el resultado sigue siendo válido
                        if (viewModel.loginResult?.success == true) {
                            android.util.Log.d("LoginScreen", "Navigating to profile")
                            onNavigateToProfile(username)
                        } else {
                            android.util.Log.e("LoginScreen", "Login result changed during delay")
                            viewModel.setError("Error: El estado del login cambió inesperadamente")
                            isNavigating = false
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("LoginScreen", "Error during navigation", e)
                        viewModel.setError("Error al navegar: ${e.message}")
                        isNavigating = false
                    }
                } else {
                    android.util.Log.e("LoginScreen", "Username is blank")
                    viewModel.setError("Error: Nombre de usuario no válido")
                }
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                showValidationError = false
                if (viewModel.errorMessage != null) {
                    viewModel.clearError()
                }
            },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading,
            isError = showValidationError || viewModel.errorMessage != null
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                showValidationError = false
                if (viewModel.errorMessage != null) {
                    viewModel.clearError()
                }
            },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading,
            isError = showValidationError || viewModel.errorMessage != null
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isBlank() || password.isBlank()) {
                    showValidationError = true
                } else {
                    showValidationError = false
                    viewModel.login(email, password)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Iniciando...")
                }
            } else {
                Text("Iniciar Sesión")
            }
        }

        // Mostrar mensaje de error de validación local
        AnimatedVisibility(
            visible = showValidationError,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = "Por favor, completa todos los campos",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Mostrar mensaje de error del servidor
        AnimatedVisibility(
            visible = viewModel.errorMessage != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            viewModel.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = error,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }

        // Mostrar mensaje de éxito
        AnimatedVisibility(
            visible = viewModel.loginResult?.success == true,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    text = "¡Login exitoso! Redirigiendo...",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Sección de registro
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "¿No tienes cuenta? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(
                onClick = {
                    val intent = android.content.Intent(android.content.Intent.ACTION_VIEW).apply {
                        data = android.net.Uri.parse("http://192.168.0.100/page/register.html")
                        addFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                    viewModel.getContext().startActivity(intent)
                }
            ) {
                Text(
                    text = "Regístrate",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}