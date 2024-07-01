package com.example.billeteramodulo4.model.remote.retrofit

import android.util.Log
import com.example.billeteramodulo4.model.remote.interceptor.AuthorizationInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitWallet {

    companion object {

        private const val BASE_URL =
            "http://wallet-main.eba-ccwdurgr.us-east-1.elasticbeanstalk.com/"

        fun getRetrofit(token: String? = null): Retrofit {
            val clientBuilder = OkHttpClient.Builder()

            if (token != null) {
                clientBuilder.addInterceptor(AuthorizationInterceptor(token))
                Log.i("USUARIO", AuthorizationInterceptor(token).toString())
            }

            val client = clientBuilder.build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}