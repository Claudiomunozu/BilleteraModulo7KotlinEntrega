package com.example.billeteramodulo4.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billeteramodulo4.model.remote.manager.Manager
import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import kotlinx.coroutines.launch


class AccountViewModel (private val useCase : UseCaseWallet, private val context: Context): ViewModel() {

    private val _accounts = MutableLiveData<List<AccountsResponse>>()
    val accounts: LiveData<List<AccountsResponse>> get() = _accounts
    private val authManager = Manager(context)

    fun fetchUserAccounts() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val response = useCase.myAccount(token)
                    if (response.isSuccessful) {
                        _accounts.value = response.body()
                    } else {
                        Log.e("AccountViewModel", "Error al obtener cuentas: ${response.message()}")
                    }
                } else {
                    Log.e("AccountViewModel", "Token de autenticación no disponible")
                }
            } catch (e: Exception) {
                // Intenta cargar las cuentas desde la base de datos local
                try {
                    val localAccounts = useCase.getLocalAccounts()
                    _accounts.value = localAccounts
                } catch (dbException: Exception) {
                    Log.e("AccountViewModel", "Error al obtener cuentas: ${e.message}")
                }
            }
        }
    }


    fun restarSaldoUsuario(monto: Int): Boolean {
        Log.i("USUARIO", "El nuevo saldo es ${_accounts.value.toString()}")
        _accounts.value?.let { userAccounts ->
            val userAccount = userAccounts[0]
            val nuevoSaldo = userAccount.money.toDouble() - monto
            Log.i("USUARIO", "El nuevo saldo es $nuevoSaldo")
            Log.i("USUARIO", "El nuevo saldo es ${_accounts.value.toString()}")
            return if (nuevoSaldo >= 0) {
                userAccount.money = nuevoSaldo.toString()
                viewModelScope.launch {
                    val token = authManager.getToken()
                    if (token != null) {
                        val response = useCase.updateAccount(token, userAccount)
                        if (response.isSuccessful) {
                            _accounts.value =
                                _accounts.value // Actualiza LiveData para notificar los cambios
                            Log.d("USUARIO", "Saldo actualizado: ${_accounts.value}")
                        } else {
                            Log.e("USUARIO", "Error al actualizar el saldo: ${response.message()}")
                        }
                    }
                }
                true
            } else {
                false
            }
        }
        return false
    }
}