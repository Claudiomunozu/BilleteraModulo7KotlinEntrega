package com.example.billeteramodulo4.model.remote.request_response

import androidx.room.Entity

@Entity(tableName = "login")
data class LoginRequest(
    val email: String,
    val password: String
)