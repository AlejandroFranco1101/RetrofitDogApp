package com.example.retrofitdogapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * Adaptador para el RecyclerView encargado de enlazar la lista de URLs de imágenes
 * con las vistas individuales representadas por DogViewHolder.
 */
class DogAdapter(
    private val images: List<String>?,
    private val onItemClickListener: ((String) -> Unit)? = null
) : RecyclerView.Adapter<DogViewHolder>() {

    // Se llama cuando RecyclerView necesita un nuevo ViewHolder para representar un elemento
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dog, parent, false)
        return DogViewHolder(view)
    }

    // Se llama para mostrar los datos en una posición específica
    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        val imageUrl = images?.getOrNull(position)
        holder.bind(imageUrl)

        // 🌟 PLUS EXTRA: Interacción al pulsar un elemento de la lista (ej. ver detalle o ampliar)
        imageUrl?.let { url ->
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(url)
            }
        }
    }

    // Devuelve el número total de elementos en el conjunto de datos
    override fun getItemCount(): Int {
        return images?.size ?: 0
    }
}
