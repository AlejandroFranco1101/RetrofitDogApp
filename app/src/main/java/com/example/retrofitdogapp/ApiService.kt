package com.example.retrofitdogapp

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Interfaz que define los endpoints del API de Dog CEO consumidos mediante Retrofit.
 * Retrofit se encarga de implementar esta interfaz en tiempo de ejecución.
 */
interface ApiService {

    /**
     * Obtiene la lista de URLs de imágenes de perros correspondientes a una raza específica.
     *
     * @GET indica que es una solicitud HTTP GET relativa a la BASE_URL.
     * @Path("raza") sustituye dinámicamente el parámetro {raza} en la URL del endpoint.
     * @param raza Nombre de la raza a consultar (por ejemplo: "hound", "labrador", "pug").
     * @return Objeto Call asíncrono que contendrá la respuesta parseada en DogsResponse.
     */
    @GET("{raza}/images")
    fun getDogsByBreed(@Path("raza") raza: String?): Call<DogsResponse?>?

    /**
     * 🌟 PLUS EXTRA: Endpoint adicional para obtener una imagen aleatoria de una raza específica.
     */
    @GET("{raza}/images/random")
    fun getRandomDogByBreed(@Path("raza") raza: String?): Call<DogsResponse?>?
}
