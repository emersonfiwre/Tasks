package com.example.tasks.service.repository.remote

import com.example.tasks.service.constants.TaskConstants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient private constructor() {

    companion object {
        private lateinit var retrofit: Retrofit
        private var personKey = ""
        private var tokenKey = ""
        private val baseUrl = "http://devmasterteam.com/CursoAndroidAPI/"
        private fun getRetrofitInstance(): Retrofit {
            if (!Companion::retrofit.isInitialized) {
                val httpClient = OkHttpClient.Builder()
                httpClient.addInterceptor { chain ->
                    val request =
                        chain.request()
                            .newBuilder()
                            .addHeader(TaskConstants.HEADER.PERSON_KEY, personKey)
                            .addHeader(TaskConstants.HEADER.TOKEN_KEY, tokenKey)
                            .build()
                    chain.proceed(request)
                }
                retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build())
                    .baseUrl(baseUrl)
                    .build()
            }

            return retrofit
        }

        fun addHeader(token: String, personKey: String) {
            this.personKey = personKey
            this.tokenKey = token
        }

        fun <S> createrService(serviceClass: Class<S>): S {
            return getRetrofitInstance().create(serviceClass)
        }
    }


}