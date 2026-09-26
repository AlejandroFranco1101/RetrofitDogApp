package com.example.retrofitdogapp

import com.google.gson.annotations.SerializedName

/**
 * Modelo de datos que representa la respuesta del API de Dog CEO (https://dog.ceo/dog-api/).
 *
 * Estructura de ejemplo del JSON:
 * {
 *    "message": [
 *        "https://images.dog.ceo/breeds/hound-afghan/n02088385_1002.jpg",
 *        "https://images.dog.ceo/breeds/hound-afghan/n02088385_1003.jpg"
 *    ],
 *    "status": "success"
 * }
 */
data class DogsResponse(
    // Campo que representa el estado de la respuesta de la API ("success" o "error")
    @SerializedName("status")
    private var status: String? = null,

    // Campo que representa la lista de URLs de imágenes
    @SerializedName("message")
    private var images: List<String?>? = null
) {
    // Obtiene el estado de la respuesta
    fun getStatus(): String? = status

    // Establece el estado de la respuesta
    fun setStatus(status: String?) {
        this.status = status
    }

    // Obtiene la lista de URLs de imágenes
    fun getImages(): List<String?>? = images

    // Establece la lista de URLs de imágenes
    fun setImages(images: List<String?>?) {
        this.images = images
    }

    /**
     * PLUS EXTRA: Método utilitario para verificar si la respuesta de la API fue exitosa.
     */
    fun isSuccess(): Boolean = status.equals("success", ignoreCase = true)
}
