package com.example.billeteramodulo4.viewmodel.viewmodelfactory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.billeteramodulo4.viewmodel.AccountViewModel
import com.example.billeteramodulo4.viewmodel.usecase.UseCaseWallet

class AccountViewModelFactory(private val useCase: UseCaseWallet, private val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
            return AccountViewModel(useCase, context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}