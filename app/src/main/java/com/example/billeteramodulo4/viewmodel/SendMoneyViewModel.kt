package com.example.billeteramodulo4.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SendMoneyViewModel :ViewModel() {

    private val _sendMoney = MutableLiveData<Int>()
    val sendMoney: LiveData<Int> get() = _sendMoney

    private val _validarSend = MutableLiveData<Boolean>()

    val validarSend: LiveData<Boolean> get() = _validarSend

    fun validarEmptySend(sendMoney : Int){
        _sendMoney.value = sendMoney
        validarSend()
    }

    private fun validarSend(){
        if (_sendMoney.value!! > 0)
        {
            _validarSend.value = true
        }else{
            _validarSend.value = false
        }
    }
}