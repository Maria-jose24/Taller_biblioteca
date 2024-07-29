package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.libro
import org.json.JSONException

class listaLibroFragment : Fragment() {

    private lateinit var recyclerViewLibro: RecyclerView
    private lateinit var adapterLibro: AdapterLibro

    private fun cargarDatos() {
        val url = "${config.urlLibros}"
        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val librosList = mutableListOf<libro>()
                    for (i in 0 until response.length()) {
                        val libroJson = response.getJSONObject(i)
                        val libro = libro(
                            libroJson.getString("id"),
                            libroJson.getString("titulo"),
                            libroJson.getString("autor"),
                            libroJson.getString("isbn"),
                            libroJson.getString("genero"),
                            libroJson.getString("numero_de_ejemplares_disponibles"),
                            libroJson.getString("numero_de_ejemplares_ocupados")
                        )
                        librosList.add(libro)
                    }
                    adapterLibro.actualizarLibros(librosList)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            },
            { error ->
                error.printStackTrace()
            }
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(jsonArrayRequest)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_libro, container, false)
        recyclerViewLibro = view.findViewById(R.id.RecyclerViewLibro)
        recyclerViewLibro.layoutManager = LinearLayoutManager(context)

        adapterLibro = AdapterLibro(emptyList(), onEditClick = { libro ->
            // Manejar clic en editar
        }, onDeleteClick = { libro ->
            // Manejar clic en eliminar
        })

        recyclerViewLibro.adapter = adapterLibro
        cargarDatos()
        return view
    }
}
