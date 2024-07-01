package com.example.billeteramodulo4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SingupPageViewModel : ViewModel() {

    private val _nombre = MutableLiveData<String>()

    val nombre: LiveData<String> get() = _nombre

    private val _apellido = MutableLiveData<String>()

    val apellido: LiveData<String> get() = _apellido

    private val _correo = MutableLiveData<String>()

    val correo: LiveData<String> get() = _correo

    private val _contrasena = MutableLiveData<String>()

    val contrasena: LiveData<String> get() = _contrasena

    private val _contrasena2 = MutableLiveData<String>()

    val contrasena2: LiveData<String> get() = _contrasena2


    private val _validarIngresoDatos = MutableLiveData<Boolean>()

    val validarIngresoDatos: LiveData<Boolean> get() = _validarIngresoDatos

    fun validarDatos(
        name: String, lastName: String,
        email: String, password: String,
        password2: String
    ) {
        _nombre.value = name
        _apellido.value = lastName
        _correo.value = email
        _contrasena.value = password
        _contrasena2.value = password2

        validarIngresoDatos()

    }

    fun validarIngresoDatos() {
        _validarIngresoDatos.value = !(_nombre.value.isNullOrEmpty() ||
                _apellido.value.isNullOrEmpty() ||
                _correo.value.isNullOrEmpty() ||
                _contrasena.value.isNullOrEmpty() ||
                _contrasena2.value.isNullOrEmpty())
    }
}