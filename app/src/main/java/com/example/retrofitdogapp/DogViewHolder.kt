package com.example.retrofitdogapp

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitdogapp.databinding.ItemDogBinding
import com.squareup.picasso.Picasso

/**
 * ViewHolder encargado de representar y enlazar la vista de cada ítem de perro (item_dog.xml)
 * utilizando View Binding y la librería Picasso para la carga asíncrona de imágenes.
 */
class DogViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    // Declaración de una instancia de ItemDogBinding para acceder a las vistas en el diseño del elemento
    // Vinculación de la vista a la clase de enlace generada para el diseño del elemento
    private val itemDogBinding: ItemDogBinding = ItemDogBinding.bind(view)

    /**
     * Vincula la URL de la imagen al ImageView en el diseño del elemento.
     * Carga la imagen desde la URL usando Picasso y la muestra en el ImageView ivDog.
     *
     * 🌟 PLUS EXTRA: Se incluye placeholder de carga visual y fallback de error
     * para prevenir parpadeos o pantallas vacías si falla la descarga de una imagen específica.
     */
    fun bind(imageUrl: String?) {
        if (!imageUrl.isNullOrBlank()) {
            Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.ic_image_placeholder)
                .error(R.drawable.ic_broken_image)
                .into(itemDogBinding.ivDog)
        } else {
            itemDogBinding.ivDog.setImageResource(R.drawable.ic_broken_image)
        }
    }
}
