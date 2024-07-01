package com.example.billeteramodulo4.model.repository

import android.util.Log
import com.example.billeteramodulo4.model.local.dao.DaoWallet
import com.example.billeteramodulo4.model.remote.request_response.LoginRequest
import com.example.billeteramodulo4.model.remote.api.ApiWallet
import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsListResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserListResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class ImplementRepository(private var apiWallet: ApiWallet, private val daoWallet: DaoWallet) :
    RepositoryWallet {

    /**
    Authetication Wallet
     **/

    /*
    hace llamado a la API para logear al usuario con metodo POST
     */
    override suspend fun login(email: String, password: String): String {
        return withContext(Dispatchers.IO) {
            val response = apiWallet.login(LoginRequest(email, password))
            Log.i("TOKEN", response.accessToken)
            response.accessToken
        }
    }

    /*
    hace llamado a la API para obtener informacion del usuario con metodo GET
     */
    override suspend fun getUserByToken(token: String): UserResponse {
        return withContext(Dispatchers.IO) {
            apiWallet.getUserByToken(token)
        }
    }

    /**
    Users Wallet
     **/

    /*
    hace llamado a la API para crear un usuario con metodo POST
     */
    override suspend fun createUser(user: UserResponse): Response<UserResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiWallet.createUser(user)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { daoWallet.insertUsers(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }

    /*
    hace llamado a la API para obtener listado de usuarios con metodo GET
     */
    override suspend fun getAllUsers(): MutableList<UserResponse> {
        return withContext(Dispatchers.IO) {
            val users = try {
                val apiUsers = apiWallet.getAllUsers()
                daoWallet.insertUsers(apiUsers)
                apiUsers
            } catch (e: Exception) {
                daoWallet.getAllUsers().toMutableList()
            }
            users
        }
    }

    /**
     * GET Users DAO y API
     */

    /*
    hace llamado al DAO o API para obtener un usuario por id con metodo GET
     */
    override suspend fun getUserById(idUser: Long): UserResponse {
        return withContext(Dispatchers.IO) {
            val user = daoWallet.getUsersByIds(listOf(idUser)).firstOrNull()
            user ?: apiWallet.getUserById(idUser).also { daoWallet.insertUsers(listOf(it)) }
        }
    }

    /*
    hace llamado al DAO o API para obtener listado de usuarios por pagina con metodo GET
     */
    override suspend fun getUsersByPage(token: String, page: Int): Response<UserListResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiWallet.getUsersByPage("Bearer $token", page)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.data?.let { daoWallet.insertUsers(it) }
                }
                apiResponse
            } catch (e: Exception) {
                val users = daoWallet.getAllUsers()
                Response.success(UserListResponse(users, users.size / 20, page))
            }
            response
        }
    }

    /**
     * Accounts Wallet
     */

    /*
    hace llamado a la API para crear una cuenta con metodo POST e inserta en BaseDatos con metodo de DAO
     */
    override suspend fun createAccount(token: String, account: AccountsResponse)
            : Response<AccountsResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiWallet.createAccount("Bearer $token", account)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { daoWallet.insertAccounts(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }

    /*
    hace llamado a la API para obtener cuenta de usuario loggeado si no obtiene respuesta va al DAO por la lista local
     */
    override suspend fun myAccount(token: String): Response<MutableList<AccountsResponse>> {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = apiWallet.myAccount("Bearer $token")
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { daoWallet.insertAccounts(it) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.success(daoWallet.getAllAccounts().toMutableList())
            }
        }
    }

    /*
    hace llamado a la API para updatear cuenta de usuario existente con metodo PUT y modifica local
     */
    override suspend fun updateAccount(token: String, account: AccountsResponse)
            : Response<AccountsResponse> {
        return withContext(Dispatchers.IO) {
            val response = try {
                val apiResponse = apiWallet.updateAccount("Bearer $token", account.id, account)
                if (apiResponse.isSuccessful) {
                    apiResponse.body()?.let { daoWallet.insertAccounts(listOf(it)) }
                }
                apiResponse
            } catch (e: Exception) {
                Response.error(500, ResponseBody.create(null, ""))
            }
            response
        }
    }

    /*
    hace llamado al DAO para obtener cuenta por id con metodo GET en caso contrario va a la API
    */
    override suspend fun getAccountsById(token: String, idAccount: Long): AccountsResponse {
        return withContext(Dispatchers.IO) {
            val account = daoWallet.getAccountsByUserId(idAccount)
            account ?: apiWallet.getAccountById("Bearer $token", idAccount)
                .also { daoWallet.insertAccounts(listOf(it)) }
        }
    }

    /**
    Transactions Wallet
     */

    /*
    hace llamado a la API para crear una transaccion con metodo POST e inserta en BaseDatos con metodo de DAO
     */
    override suspend fun createTransaction(
        token: String,
        transaction: TransactionsResponse
    ): Response<TransactionsResponse> {
        return withContext(Dispatchers.IO) {
            val response = apiWallet.createTransaction("Bearer $token", transaction)
            if (response.isSuccessful) {
                response.body()?.let { daoWallet.insertTransactions(listOf(it)) }
            }
            response
        }
    }

    /*
    hace llamado a la API para obtener listado de transacciones de usuario loggeado si no obtiene respuesta va al DAO por la lista local
     */
    override suspend fun getAllTransactionUser(token: String): TransactionsListResponse {
        return withContext(Dispatchers.IO) {
            try {
                val apiResponse = apiWallet.getAllTransactionUser("Bearer $token")
                daoWallet.insertTransactions(apiResponse.data)
                apiResponse
            } catch (e: Exception) {
                TransactionsListResponse(null, null, daoWallet.getAllTransactions())
            }
        }
    }

    /**
     * Metodos Locales
     */
    override suspend fun getLocalUser(): UserResponse {
        return withContext(Dispatchers.IO) {
            daoWallet.getUser() ?: throw NoSuchElementException("User not found locally")
        }
    }

    override suspend fun getLocalAccounts(): List<AccountsResponse> {
        return withContext(Dispatchers.IO) {
            daoWallet.getAllAccounts()
        }
    }

    override suspend fun getLocalTransactions(): List<TransactionsResponse> {
        return withContext(Dispatchers.IO) {
            daoWallet.getAllTransactions()
        }
    }

    override suspend fun getLocalUsers(page: Int): List<UserResponse> {
        return withContext(Dispatchers.IO) {
            daoWallet.getAllUsers()
        }
    }
}
