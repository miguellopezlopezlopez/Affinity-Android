package com.miguel.affinity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miguel.affinity.ui.auth.LoginScreen
import com.miguel.affinity.ui.profile.ProfileScreen
import com.miguel.affinity.ui.theme.AffinityTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        android.util.Log.d("MainActivity", "onCreate called")
        enableEdgeToEdge()
        setContent {
            android.util.Log.d("MainActivity", "Setting content")
            AffinityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AffinityNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        android.util.Log.d("MainActivity", "onStart called")
    }

    override fun onResume() {
        super.onResume()
        android.util.Log.d("MainActivity", "onResume called")
    }

    override fun onPause() {
        super.onPause()
        android.util.Log.d("MainActivity", "onPause called")
    }

    override fun onStop() {
        super.onStop()
        android.util.Log.d("MainActivity", "onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        android.util.Log.d("MainActivity", "onDestroy called")
    }
}

@Composable
fun AffinityNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    android.util.Log.d("AffinityNavigation", "Composing navigation")
    
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            android.util.Log.d("AffinityNavigation", "Composing login screen")
            LoginScreen(
                onNavigateToProfile = { username ->
                    try {
                        android.util.Log.d("AffinityNavigation", "Attempting to navigate to profile with username: $username")
                        // Navegar al perfil y limpiar el back stack para evitar volver al login
                        navController.navigate("profile/$username") {
                            // Configuración más suave de la navegación
                            popUpTo(0) { inclusive = true }  // Cambio aquí: popUpTo(0) en lugar de "login"
                            launchSingleTop = true
                        }
                    } catch (e: Exception) {
                        android.util.Log.e("AffinityNavigation", "Navigation error", e)
                    }
                }
            )
        }

        composable(
            route = "profile/{username}",
            arguments = listOf(
                navArgument("username") {
                    type = NavType.StringType
                    nullable = false
                    defaultValue = ""  // Agregado valor por defecto
                }
            )
        ) { backStackEntry ->
            android.util.Log.d("AffinityNavigation", "Composing profile screen")
            val username = backStackEntry.arguments?.getString("username") ?: ""
            android.util.Log.d("AffinityNavigation", "Profile username: $username")
            
            if (username.isNotBlank()) {
                ProfileScreen(
                    username = username,
                    onLogout = {
                        android.util.Log.d("AffinityNavigation", "Logout requested")
                        navController.navigate("login") {
                            popUpTo(0) { inclusive = true }  // Cambio aquí: popUpTo(0) en lugar de ruta específica
                        }
                    },
                    onShowMatches = {
                        android.util.Log.d("AffinityNavigation", "Show matches requested")
                    },
                    onShowMessages = {
                        android.util.Log.d("AffinityNavigation", "Show messages requested")
                    },
                    onShowStats = {
                        android.util.Log.d("AffinityNavigation", "Show stats requested")
                    }
                )
            } else {
                android.util.Log.e("AffinityNavigation", "Empty username, redirecting to login")
                LaunchedEffect(Unit) {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }  // Cambio aquí: popUpTo(0) en lugar de ruta específica
                    }
                }
            }
        }
    }
}