package com.example.billeteramodulo4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RequestMoneyViewModel : ViewModel() {

    private val _requestMoney = MutableLiveData<Int>()
    val requestMoney: LiveData<Int> get() = _requestMoney

    private val _validarRequest = MutableLiveData<Boolean>()

    val validarRequest: LiveData<Boolean> get() = _validarRequest

    fun validarEmpty(requestMoney: Int)  {
        _requestMoney.value = requestMoney
        validarRequest()

    }

    private fun validarRequest() {
        if (_requestMoney.value!! > 0)
        {
            _validarRequest.value = true
        } else {
            _validarRequest.value = false
        }
    }

}