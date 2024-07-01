package com.example.billeteramodulo4.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse

@Dao
interface DaoWallet {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserResponse>)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserResponse>

    @Query("SELECT * FROM users WHERE id IN (:userIds)")
    suspend fun getUsersByIds(userIds: List<Long>): List<UserResponse>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountsResponse>)

    @Query("SELECT * FROM accounts")
    suspend fun getAllAccounts(): List<AccountsResponse>

    @Query("SELECT * FROM accounts WHERE userId = :userId")
    suspend fun getAccountsByUserId(userId: Long): AccountsResponse

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionsResponse>)

    @Query("SELECT * FROM transactions WHERE userId = :userId")
    suspend fun getTransactionsByUserId(userId: Long): List<TransactionsResponse>

    @Query("SELECT * FROM transactions")
    suspend fun getAllTransactions(): List<TransactionsResponse>

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUser(): UserResponse?
}