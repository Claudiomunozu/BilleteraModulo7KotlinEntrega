package com.example.billeteramodulo4.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billeteramodulo4.model.remote.manager.Manager
import com.example.billeteramodulo4.model.remote.request_response.TransactionsListResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TransactionViewModel(private val useCase: UseCaseWallet, private val context: Context) :
    ViewModel() {

    private val _transactions = MutableLiveData<TransactionsListResponse>()
    val transactions: LiveData<TransactionsListResponse> get() = _transactions

    private val _transaction = MutableLiveData<TransactionsResponse?>()
    val transaction: MutableLiveData<TransactionsResponse?> get() = _transaction

    private val authManager = Manager(context)

    fun createTransaction(
        amount: Long,
        concept: String,
        type: String,
        accountId: Long,
        userId: Long
    ) {
        viewModelScope.launch {
            // Obtener el token de autenticación
            val token = authManager.getToken()
            if (token != null) {
                // Construir el objeto de transacción
                val currentDate = Calendar.getInstance().time
                val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
                val formattedDate = dateFormat.format(currentDate)

                val transaction = TransactionsResponse(
                    id = 0,
                    amount = amount,
                    concept = concept,
                    date = formattedDate,  // Fecha actual
                    type = type,  // Tipo de transacción (suponiendo que es un pago)
                    accountId = accountId,  // ID de la cuenta del usuario que realiza la transacción
                    userId = userId,  // ID del usuario que realiza la transacción
                    toAccountId = 5  // ID de la cuenta destino
                )
                try {
                    // Llamar al caso de uso para crear la transacción
                    val response = useCase.createTransaction(token, transaction)

                    if (response.isSuccessful) {
                        val createdTransaction = response.body()
                        _transaction.value = createdTransaction

                        Log.d("USUARIO", "Transacción creada: ${createdTransaction?.id}")

                        // Agregar la nueva transacción a la lista existente
                        createdTransaction?.let {
                            val updatedTransactions =
                                _transactions.value?.data?.toMutableList() ?: mutableListOf()
                            updatedTransactions.add(it)

                            _transactions.value = TransactionsListResponse(
                                previousPage = _transactions.value?.previousPage,
                                nextPage = _transactions.value?.nextPage,
                                data = updatedTransactions
                            )
                        }
                    } else {
                        Log.e("USUARIO", "Error al crear la transacción: ${response.message()}")
                    }
                } catch (e: Exception) {
                    Log.e("USUARIO", "Excepción al crear la transacción: ${e.message}")
                }
            } else {
                Log.e("USUARIO", "No se pudo obtener el token de autenticación")
            }
        }
    }

    fun getAlltransactions() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val transactions = useCase.getAllTransactionUser(token)
                    _transactions.value = transactions
                    Log.i("USUARIO", "Las transacciones son: " + _transactions.value.toString())
                    transactions.data.forEach { transaction ->
                        Log.i(
                            "USUARIO",
                            "Transacción - ID: ${transaction.id}, Amount: ${transaction.amount}, Concept: ${transaction.concept}"
                        )
                    }
                } else {
                    Log.e("USUARIO", "No se pudo obtener el token de autenticación")
                }
            } catch (e: Exception) {
                // Intenta cargar las transacciones desde la base de datos local
                try {
                    val localTransactions = useCase.getLocalTransactions()
                    _transactions.value = TransactionsListResponse(
                        previousPage = null,
                        nextPage = null,
                        data = localTransactions
                    )
                } catch (dbException: Exception) {
                    Log.e(
                        "TransactionViewModel",
                        "Error al obtener las transacciones: ${e.message}"
                    )
                }
            }
        }
    }
}