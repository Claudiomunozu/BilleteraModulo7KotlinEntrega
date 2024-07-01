package com.example.billeteramodulo4.view

import android.os.Bundle
import android.util.Log

import androidx.appcompat.app.AppCompatActivity
import com.example.billeteramodulo4.R
import com.example.billeteramodulo4.model.remote.manager.Manager

class MainActivity : AppCompatActivity() {
    private lateinit var manager : Manager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        manager = Manager(this)

        // Obtiene el token
        val token = manager.getToken()
        if (token != null) {
            Log.d("TOKEN-MAIN", token)
            manager.clearToken()
        } else {
            return
        }
    }

    override fun onStop() {
        super.onStop()
        manager.clearToken()
        manager.clearUser()
    }

    override fun onDestroy() {
        super.onDestroy()
        manager.clearToken()
        manager.clearUser()
    }
}