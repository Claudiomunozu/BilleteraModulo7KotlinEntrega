package com.example.billeteramodulo4.model.remote.request_response

/**
 * data class que recibe Token para validar usuario desde la API
 */
data class LoginResponse(
    val accessToken: String
)