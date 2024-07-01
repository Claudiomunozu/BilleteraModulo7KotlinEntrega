package com.example.billeteramodulo4.model.remote.api

import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.LoginRequest
import com.example.billeteramodulo4.model.remote.request_response.LoginResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsListResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserListResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiWallet {

    /**
     * METODOS PARA SOLICITUDES A LA API
     */

    /*
    Authetication API
    */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    @GET("auth/me")
    suspend fun getUserByToken(@Header("Authorization") token: String): UserResponse

    /*
    Users API
     */
    @POST("users")
    suspend fun createUser(@Body user: UserResponse): Response<UserResponse>

    @GET("users") //Falta agregar el token ****************************************
    suspend fun getAllUsers(): MutableList<UserResponse>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") idUser: Long): UserResponse

    @GET("users")
    suspend fun getUsersByPage(
        @Header("Authorization") token: String,
        @Query("page") page: Int
    ): Response<UserListResponse>

    /*
    Accounts API
     */
    @POST("accounts")
    suspend fun createAccount(
        @Header("Authorization") token: String,
        @Body account: AccountsResponse
    ): Response<AccountsResponse>

    @GET("accounts/me")
    suspend fun myAccount(@Header("Authorization") token: String): Response<MutableList<AccountsResponse>>

    @PUT("accounts/{id}")
    suspend fun updateAccount(
        @Header("Authorization") token: String,
        @Path("id") id: Long,
        @Body account: AccountsResponse
    ): Response<AccountsResponse>

    @GET("accounts/{id}")
    suspend fun getAccountById(
        @Header("Authorization") token: String,
        @Path("id") id: Long
    ): AccountsResponse

    /*
    Transactions API
     */
    @POST("transactions")
    suspend fun createTransaction(
        @Header("Authorization") token: String,
        @Body transaction: TransactionsResponse
    ): Response<TransactionsResponse>

    @GET("transactions")
    suspend fun getAllTransactionUser(@Header("Authorization") token: String): TransactionsListResponse
}