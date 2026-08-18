package com.memori.app.data.remote

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Durante desenvolvimento: IP do seu computador na rede Wi-Fi
    // Exemplo: "http://192.168.1.105:3000/"
    // Depois de publicar o Node: substitui pela URL real
    private const val BASE_URL = "https://memoricrm-ten.vercel.app/"

    var token: String = ""

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            chain.proceed(request)
        }
        .build()

    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}