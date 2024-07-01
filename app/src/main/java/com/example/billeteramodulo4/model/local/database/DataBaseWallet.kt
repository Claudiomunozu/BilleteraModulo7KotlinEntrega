package com.example.billeteramodulo4.model.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.billeteramodulo4.model.local.dao.DaoWallet
import com.example.billeteramodulo4.model.remote.request_response.AccountsResponse
import com.example.billeteramodulo4.model.remote.request_response.TransactionsResponse
import com.example.billeteramodulo4.model.remote.request_response.UserResponse

@Database(
    entities = [UserResponse::class, AccountsResponse::class, TransactionsResponse::class],
    version = 1,
    exportSchema = false
)

abstract class DataBaseWallet : RoomDatabase() {

    abstract fun daoWallet(): DaoWallet

    companion object {
        @Volatile
        private var INSTANCE: DataBaseWallet? = null

        fun getDataBase(context: Context): DataBaseWallet {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataBaseWallet::class.java,
                    "wallet_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}