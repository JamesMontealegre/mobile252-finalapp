package com.compose.finalapp.viewmodel

import androidx.lifecycle.ViewModel
import com.compose.finalapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class AuthViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _loggedUser = MutableStateFlow<User?>(null)
    val loggedUser: StateFlow<User?> = _loggedUser

    fun updateEmail(v: String) {
        _email.value = v
    }

    fun updatePassword(v: String) {
        _password.value = v
    }

    fun updateName(v: String) {
        _name.value = v
    }

    /** Registrar usuario **/
    fun register(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val newUser = hashMapOf(
            "email" to email.value,
            "password" to password.value,
            "name" to name.value,
            "sessionActive" to false
        )

        db.collection("users")
            .whereEqualTo("email", email.value)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) onError("El correo ya está registrado")
                else {
                    db.collection("users").add(newUser)
                        .addOnSuccessListener { onSuccess() }
                        .addOnFailureListener { e ->
                            onError(e.message ?: "Error al registrar")
                        }
                }
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error en la base de datos") }
    }

    /** Login **/
    fun login(onSuccess: () -> Unit, onError: (String) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email.value)
            .whereEqualTo("password", password.value)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val doc = result.documents.first()
                    val userId = doc.id
                    val user =
                        doc.toObject(User::class.java)?.copy(id = userId, sessionActive = true)

                    if (user != null) {
                        _loggedUser.value = user
                        db.collection("users").document(userId).update("sessionActive", true)
                        onSuccess()
                    }
                } else onError("Credenciales incorrectas")
            }
            .addOnFailureListener { e -> onError(e.message ?: "Error de conexión") }
    }

    /** Logout **/
    fun logout(onSuccess: () -> Unit) {
        _loggedUser.value?.let { user ->
            db.collection("users").document(user.id)
                .update("sessionActive", false)
                .addOnSuccessListener {
                    _loggedUser.value = null
                    onSuccess()
                }
        }
    }

    /** Comprobar sesión activa **/
    fun checkSession(onFound: (User) -> Unit) {
        db.collection("users")
            .whereEqualTo("sessionActive", true)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val doc = result.documents.first()
                    val user = doc.toObject(User::class.java)?.copy(id = doc.id)
                    if (user != null) onFound(user)
                }
            }
    }
}
