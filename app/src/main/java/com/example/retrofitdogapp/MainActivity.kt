package com.example.retrofitdogapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.retrofitdogapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dogAdapter: DogAdapter
    private var images: MutableList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initRecyclerView()
        binding.searchDogs.setOnQueryTextListener(this as SearchView.OnQueryTextListener)

        // 🌟 PLUS EXTRA: Carga inicial para mostrar fotos al abrir la app
        searchByName("hound")
    }

    private fun initRecyclerView() {
        // 🌟 PLUS EXTRA: Toast interactivo al tocar una imagen
        dogAdapter = DogAdapter(images) { imageUrl ->
            Toast.makeText(this, "Imagen: $imageUrl", Toast.LENGTH_SHORT).show()
        }
        binding.listDogs.layoutManager = LinearLayoutManager(this)
        binding.listDogs.adapter = dogAdapter
    }

    private fun searchByName(query: String) {
        var cleanQuery = query.trim().lowercase(Locale.getDefault())
        if (cleanQuery.isEmpty()) return

        var displayName = cleanQuery

        // 🇸🇻 SUPER PLUS: Raza salvadoreña "Aguacatero" / "Chucho criollo"
        if (cleanQuery.contains("aguacatero") || cleanQuery.contains("chucho") || cleanQuery.contains("criollo")) {
            Toast.makeText(this, "🇸🇻 ¡Cargando el auténtico chucho aguacatero salvadoreño!", Toast.LENGTH_LONG).show()
            cleanQuery = "mix"
            displayName = "Aguacatero Salvadoreño"
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.layoutEmpty.visibility = View.GONE

        val parts = cleanQuery.split("\\s+".toRegex()).filter { it.isNotBlank() }

        if (parts.size == 1) {
            // Consulta de raza simple (ej. hound, pug, husky, mix)
            val call = RetrofitClient.instance.getDogsByBreed(parts[0])
            executeCall(call, null, displayName)
        } else if (parts.size == 2) {
            // 🌟 SUPER PLUS: Soporte inteligente para sub-razas compuestas
            // Ej: "scottish terrier" -> intenta terrier/scottish, o con fallback a scottish/terrier (ej. german/shepherd)
            val primaryCall = RetrofitClient.instance.getDogsBySubBreed(parts[1], parts[0])
            val fallbackCall = RetrofitClient.instance.getDogsBySubBreed(parts[0], parts[1])
            executeCall(primaryCall, fallbackCall, displayName)
        } else {
            val call = RetrofitClient.instance.getDogsByBreed(parts[0])
            executeCall(call, null, displayName)
        }
    }

    private fun executeCall(
        call: Call<DogsResponse?>?,
        fallbackCall: Call<DogsResponse?>?,
        displayName: String
    ) {
        call?.enqueue(object : Callback<DogsResponse?> {
            override fun onResponse(call: Call<DogsResponse?>, response: Response<DogsResponse?>) {
                if (response.isSuccessful && response.body() != null) {
                    val responseImages: List<String> = (response.body()!!.getImages() ?: emptyList()).filterNotNull()
                    if (responseImages.isNotEmpty()) {
                        binding.progressBar.visibility = View.GONE
                        images.clear()
                        images.addAll(responseImages)
                        dogAdapter.notifyDataSetChanged()
                        binding.listDogs.visibility = View.VISIBLE
                        binding.layoutEmpty.visibility = View.GONE
                        return
                    }
                }

                // Si falló el primer orden jerárquico, intentamos el fallback (subraza/raza invertida)
                if (fallbackCall != null) {
                    executeCall(fallbackCall, null, displayName)
                } else {
                    binding.progressBar.visibility = View.GONE
                    showEmptyState("No se encontraron fotos para \"$displayName\". Prueba con: aguacatero, scottish terrier, husky, pug...")
                }
            }

            override fun onFailure(call: Call<DogsResponse?>, t: Throwable) {
                if (fallbackCall != null) {
                    executeCall(fallbackCall, null, displayName)
                } else {
                    binding.progressBar.visibility = View.GONE
                    showError("Error de red: verifica tu conexión a Internet")
                }
            }
        })
    }

    private fun showError(message: String = "Ocurrió un error") {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showEmptyState(message: String) {
        images.clear()
        dogAdapter.notifyDataSetChanged()
        binding.listDogs.visibility = View.GONE
        binding.layoutEmpty.visibility = View.VISIBLE
        binding.tvEmptyMessage.text = message
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.root.windowToken, 0)
        binding.searchDogs.clearFocus()
    }

    override fun onQueryTextChange(query: String): Boolean {
        if (query.isNotEmpty()) {
            searchByName(query.lowercase(Locale.getDefault()))
        } else {
            images.clear()
            dogAdapter.notifyDataSetChanged()
            binding.layoutEmpty.visibility = View.GONE
        }
        return true
    }

    override fun onQueryTextSubmit(newText: String?): Boolean {
        if (!newText.isNullOrBlank()) {
            searchByName(newText)
            hideKeyboard()
        }
        return true
    }
}