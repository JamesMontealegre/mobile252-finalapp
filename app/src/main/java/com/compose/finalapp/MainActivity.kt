package com.compose.finalapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import com.compose.finalapp.screen.*
import com.compose.finalapp.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    private val viewModel = AuthViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var currentScreen by remember { mutableStateOf("loading") }

            LaunchedEffect(Unit) {
                viewModel.checkSession {
                    viewModel.updateEmail(it.email)
                    currentScreen = "home"
                }
                if (currentScreen == "loading") currentScreen = "login"
            }

            MaterialTheme {
                when (currentScreen) {
                    "login" -> LoginScreen(
                        viewModel = viewModel,
                        onLoginSuccess = { currentScreen = "home" },
                        onGoRegister = { currentScreen = "register" }
                    )
                    "register" -> RegisterScreen(
                        viewModel = viewModel,
                        onRegisterSuccess = { currentScreen = "login" },
                        onGoLogin = { currentScreen = "login" }
                    )
                    "home" -> HomeScreen(
                        viewModel = viewModel,
                        onLogout = { currentScreen = "login" }
                    )
                }
            }
        }
    }
}
