package com.example.billeteramodulo4.model.repository

import com.example.billeteramodulo4.model.local.entities.AccountsLocal
import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsListResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserListResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import retrofit2.Response

interface RepositoryWallet {

    suspend fun login(email: String, password: String): String

    suspend fun getUserByToken(token: String): UserResponse

    suspend fun createUser(user: UserResponse): Response<UserResponse>

    suspend fun getAllUsers(): MutableList<UserResponse>

    suspend fun getUserById(idUser: Long): UserResponse

    suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse>

    suspend fun createAccount(token: String, account: AccountsResponse): Response<AccountsResponse>

    suspend fun myAccount(token: String): Response<MutableList<AccountsResponse>>

    suspend fun updateAccount(token: String, account: AccountsResponse): Response<AccountsResponse>

    suspend fun getAccountsById(token: String, idAccount: Long): AccountsResponse

    suspend fun createTransaction(token: String, transaction: TransactionsResponse): Response<TransactionsResponse>

    suspend fun getAllTransactionUser(token: String): TransactionsListResponse

    /*
    local
     */

    suspend fun getLocalUser(): UserResponse

    suspend fun getLocalAccounts(): List<AccountsResponse>

    suspend fun getLocalTransactions(): List<TransactionsResponse>

    suspend fun getLocalUsers(page: Int): List<UserResponse>
}