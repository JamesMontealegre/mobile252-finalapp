package com.compose.finalapp.model

data class User(
    val id: String = "",
    val email: String = "",
    val password: String = "",
    val name: String = "",
    val sessionActive: Boolean = false
)
