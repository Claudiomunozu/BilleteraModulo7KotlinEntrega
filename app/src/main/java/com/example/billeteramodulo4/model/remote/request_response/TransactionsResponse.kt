package com.example.billeteramodulo4.model.remote.request_response

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "transactions")
data class TransactionsResponse (
@PrimaryKey
val id: Long,
val amount: Long,
val concept: String,
val date: String,
val type: String,
val accountId: Long,
val userId: Long,
@SerializedName("to_account_id")
val toAccountId: Long
)