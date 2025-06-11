package com.miguel.affinity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.miguel.affinity.ui.auth.LoginScreen
import com.miguel.affinity.ui.profile.ProfileScreen
import com.miguel.affinity.ui.theme.AffinityTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AffinityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AffinityNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun AffinityNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "login",
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToProfile = { username ->
                    // Navegar al perfil y limpiar el back stack para evitar volver al login
                    navController.navigate("profile/$username") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("profile/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            ProfileScreen(
                username = username,
                onLogout = {
                    // Navegar de vuelta al login
                    navController.navigate("login") {
                        popUpTo("profile/{username}") { inclusive = true }
                    }
                },
                onShowMatches = {
                    // Navegar a matches - puedes implementar esto después
                    // navController.navigate("matches")
                },
                onShowMessages = {
                    // Navegar a mensajes - puedes implementar esto después
                    // navController.navigate("messages")
                },
                onShowStats = {
                    // Navegar a estadísticas - puedes implementar esto después
                    // navController.navigate("stats")
                }
            )
        }
    }
}