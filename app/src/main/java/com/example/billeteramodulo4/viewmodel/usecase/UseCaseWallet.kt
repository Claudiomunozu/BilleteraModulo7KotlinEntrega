package com.example.billeteramodulo4.viewmodel.usecase

import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsListResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserListResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import com.example.billeteramodulo4.model.repository.ImplementRepository
import retrofit2.Response

class UseCaseWallet(private val repository: ImplementRepository) {


    /*
    Authetication
    */
    suspend fun login(email: String, password: String): String = repository.login(email, password)
    suspend fun getUserByToken(token: String): UserResponse = repository.getUserByToken(token)

    /*
    Users
    */
    suspend fun createUserApp(user: UserResponse): Response<UserResponse> =
        repository.createUser(user)

    /*
    AccountsResponse
    */
    suspend fun createAccount(
        token: String,
        account: AccountsResponse
    ): Response<AccountsResponse> = repository.createAccount(token, account)

    suspend fun myAccount(token: String): Response<MutableList<AccountsResponse>> =
        repository.myAccount(token)

    suspend fun updateAccount(
        token: String,
        account: AccountsResponse
    ): Response<AccountsResponse> = repository.updateAccount(token, account)

    /*
    Transactions
    */
    suspend fun createTransaction(
        token: String,
        transaction: TransactionsResponse
    ): Response<TransactionsResponse> {
        return repository.createTransaction(token, transaction)
    }

    suspend fun getAllTransactionUser(token: String): TransactionsListResponse =
        repository.getAllTransactionUser(token)

    /*
    Local
     */
    suspend fun getLocalUsers(page: Int): List<UserResponse> {
        return repository.getLocalUsers(page)
    }

    suspend fun getLocalUser(): UserResponse {
        return repository.getLocalUser()
    }

    suspend fun getLocalAccounts(): List<AccountsResponse> = repository.getLocalAccounts()
    suspend fun getLocalTransactions(): List<TransactionsResponse> =
        repository.getLocalTransactions()
}