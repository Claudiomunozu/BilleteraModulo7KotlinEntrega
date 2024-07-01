package com.example.billeteramodulo4.model.local.entities

data class User(

    val name: String,
    val lastName: String,
    val email: String?,
    val password: String?,
    val password2: String?
)
