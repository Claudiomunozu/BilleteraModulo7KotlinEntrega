package com.example.billeteramodulo4.model.remote.request_response

data class TransactionsListResponse(
    val previousPage: String?,
    val nextPage: String?,
    val data: List<TransactionsResponse>
)