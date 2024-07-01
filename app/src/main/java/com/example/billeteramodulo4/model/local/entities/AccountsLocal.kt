package com.example.billeteramodulo4.model.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class AccountsLocal(
    @PrimaryKey
    val id: Long,
    val creationDate: String,
    var money: String,
    val isBlocked: Boolean,
    val userId: Long
)