package com.compose.finalapp.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compose.finalapp.viewmodel.AuthViewModel

@Composable
fun HomeScreen(viewModel: AuthViewModel, onLogout: () -> Unit) {
    val user by viewModel.loggedUser.collectAsState()

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Bienvenido, ${user?.name ?: "Usuario"}",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(24.dp))
            Button(onClick = { viewModel.logout(onSuccess = onLogout) }) {
                Text("Cerrar sesión")
            }
        }
    }
}
