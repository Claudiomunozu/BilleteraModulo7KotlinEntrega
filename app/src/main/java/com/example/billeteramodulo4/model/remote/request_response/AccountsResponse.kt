package com.example.billeteramodulo4.model.remote.request_response

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountsResponse(
    @PrimaryKey
    val id: Long,
    val creationDate: String,
    var money: String,
    val isBlocked: Boolean,
    val userId: Long
)