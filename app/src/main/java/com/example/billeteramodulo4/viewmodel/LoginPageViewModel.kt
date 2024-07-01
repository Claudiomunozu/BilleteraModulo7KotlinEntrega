package com.example.billeteramodulo4.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.billeteramodulo4.model.remote.manager.Manager
import com.example.billeteramodulo4.model.remote.request_response.UserResponse
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet
import kotlinx.coroutines.launch
import retrofit2.HttpException



class LoginPageViewModel (private val useCase: UseCaseWallet, private val context: Context): ViewModel() {

    private val _email = MutableLiveData<String?>()
    val email: LiveData<String?> get() = _email

    private val _password = MutableLiveData<String?>()
    val password: LiveData<String?> get() = _password

    private val _validarUsuario = MutableLiveData<Boolean>()
    val validarUsuario: LiveData<Boolean> get() = _validarUsuario

    private val _user = MutableLiveData<UserResponse>()
    val user: LiveData<UserResponse> get() = _user

    private val _createdUserId = MutableLiveData<Long>()
    val createdUserId: LiveData<Long> get() = _createdUserId

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    private val _usuarioLogueado = MutableLiveData<UserResponse>()
    val usuarioLogueado: LiveData<UserResponse> get() = _usuarioLogueado

    private val authManager = Manager(context)

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun createUser(user: UserResponse) {
        viewModelScope.launch {
            try {
                val response = useCase.createUserApp(user)
                if (response.isSuccessful) {
                    val createdUser = response.body()
                    createdUser?.id?.let {
                        _createdUserId.value = it
                        Log.d("USUARIO", "NUEVO USUARIO SU ID: $it")
                        Toast.makeText(context, "User creado con éxito", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    _error.value = "Error al crear usuario: ${response.message()}"
                    Log.e("USUARIO", "Error al crear usuario: ${response.message()}")
                    Toast.makeText(
                        context,
                        "User ya existe con Los datos ingresados, por favor intente con nuevos datos",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                _error.value = "Error al crear usuario: ${e.message}"
                Log.e("USUARIO", "Error al crear usuario: ${e.message}")
                Toast.makeText(context, "Error al crear usuario", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val token = useCase.login(email, password)
                authManager.saveToken(token)
                _token.value = token
                Log.e("USUARIO", _token.value.toString())
            } catch (e: HttpException) {
                _error.value = "Error: ${e.code()} ${e.message()}"
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _token.value = null
            }
        }
    }

    fun fetchLoggedUser() {
        viewModelScope.launch {
            try {
                val token = authManager.getToken()
                if (token != null) {
                    val usuario = useCase.getUserByToken(token)
                    _usuarioLogueado.value = usuario
                    Log.i("USUARIO", _usuarioLogueado.value.toString())
                } else {
                    Log.e("USUARIO", "No se pudo obtener el token de autenticación")
                }
            } catch (e: Exception) {
                // Intenta cargar el usuario desde la base de datos local
                try {
                    val localUser = useCase.getLocalUser()
                    _usuarioLogueado.value = localUser
                } catch (dbException: Exception) {
                    Log.e("USUARIO", "Error al obtener el usuario: ${e.message}")
                    _error.value = "Error al obtener el usuario: ${e.message}"
                }
            }
        }
    }
}