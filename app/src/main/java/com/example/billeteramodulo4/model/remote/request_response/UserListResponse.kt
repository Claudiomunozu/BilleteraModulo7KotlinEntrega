package com.example.billeteramodulo4.model.remote.request_response

data class UserListResponse(
    val data: List<UserResponse>, // La lista de usuarios de la página actual
    val totalPages: Int, // Total de páginas disponibles
    val currentPage: Int // Página actual
)