package com.miguel.affinity.ui.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.miguel.affinity.data.model.UserProfile

@Composable
fun ProfileScreen(
    username: String,
    viewModel: ProfileViewModel = androidx.hilt.navigation.compose.hiltViewModel(),
    onLogout: () -> Unit,
    onShowMatches: () -> Unit,
    onShowMessages: () -> Unit,
    onShowStats: () -> Unit
) {
    val profile by viewModel.profile.collectAsState()
    val message by viewModel.message.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var ubicacion by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var foto by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Cargar datos al entrar
    LaunchedEffect(Unit) {
        android.util.Log.d("ProfileScreen", "Initial load for username: $username")
        viewModel.loadProfile(username)
    }

    // Actualiza los campos cuando cambia el perfil
    LaunchedEffect(profile) {
        android.util.Log.d("ProfileScreen", "Profile updated: $profile")
        profile?.let {
            nombre = it.nombre
            apellido = it.apellido
            genero = it.genero
            ubicacion = it.ubicacion
            foto = it.foto
        }
    }

    // Mostrar error si existe
    LaunchedEffect(error) {
        error?.let {
            android.util.Log.e("ProfileScreen", "Error detected: $it")
            // Aquí podrías mostrar un Snackbar o Dialog con el error
        }
    }

    val context = LocalContext.current
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            foto = it.toString()
            // Aquí deberías subir la imagen al servidor y actualizar la URL en el perfil
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = error ?: "Error desconocido",
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
                Button(
                    onClick = {
                        viewModel.clearError()
                        viewModel.loadProfile(username)
                    }
                ) {
                    Text("Reintentar")
                }
                Button(
                    onClick = onLogout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Volver al login")
                }
            }
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Perfil de $username",
            style = MaterialTheme.typography.titleLarge
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        // Foto de perfil
        if (foto.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(foto),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        }
        
        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Cargar foto")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = apellido,
            onValueChange = { apellido = it },
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = ubicacion,
            onValueChange = { ubicacion = it },
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                profile?.let {
                    viewModel.updateProfile(
                        it.copy(
                            nombre = nombre,
                            apellido = apellido,
                            genero = genero,
                            ubicacion = ubicacion,
                            foto = foto
                        ),
                        password
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Actualizar datos")
        }

        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { showDeleteDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Eliminar cuenta")
        }

        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(onClick = onShowMatches) { Text("Matches") }
            Button(onClick = onShowMessages) { Text("Mensajes") }
            Button(onClick = onShowStats) { Text("Estadísticas") }
            Button(onClick = onLogout) { Text("Cerrar sesión") }
        }

        message?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.") },
            confirmButton = {
                Button(
                    onClick = {
                        profile?.let {
                            viewModel.deleteAccount(it.id)
                        }
                        showDeleteDialog = false
                        onLogout()
                    }
                ) {
                    Text("Sí, eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { showDeleteDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}