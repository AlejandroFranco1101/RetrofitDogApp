package com.example.retrofitdogapp

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Objeto Singleton que gestiona la configuración e inicialización de Retrofit.
 * Utiliza inicialización perezosa (by lazy) para garantizar que los recursos de red
 * solo se asignen cuando se realiza la primera petición.
 */
object RetrofitClient {

    private const val BASE_URL = "https://dog.ceo/api/breed/"

    /**
     * 🌟 PLUS EXTRA: Cliente HTTP optimizado con tiempos de espera (timeouts) explícitos
     * para evitar que la aplicación quede bloqueada ante conexiones inestables.
     */
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    /**
     * Instancia singleton de ApiService generada mediante Retrofit.
     */
    val instance: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }
}
