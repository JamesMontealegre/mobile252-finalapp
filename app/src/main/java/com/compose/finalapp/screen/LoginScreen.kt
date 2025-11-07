package com.compose.finalapp.screen

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.compose.finalapp.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onGoRegister: () -> Unit
) {
    val email by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(16.dp))
        TextField(value = email, onValueChange = { viewModel.updateEmail(it) }, label = { Text("Email") })
        Spacer(Modifier.height(8.dp))
        TextField(
            value = password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = {
            viewModel.login(
                onSuccess = { onLoginSuccess() },
                onError = { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
            )
        }) {
            Text("Entrar")
        }
        Spacer(Modifier.height(8.dp))
        TextButton(onClick = { onGoRegister() }) { Text("Crear cuenta") }
    }
}
