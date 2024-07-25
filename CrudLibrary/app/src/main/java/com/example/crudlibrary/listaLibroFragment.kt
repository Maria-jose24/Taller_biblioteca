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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class listaLibroFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recyclerView: RecyclerView
    //private lateinit var libroAdapter: LibroAdapter
    private val libros = mutableListOf<libro>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lista_libro, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewLibros)
        recyclerView.layoutManager = LinearLayoutManager(context)
        /*libroAdapter = LibroAdapter(requireContext(), libros, ::onEditClick, ::onDeleteClick)
        recyclerView.adapter = libroAdapter


        cargar_libros()

         */
        return view
    }
/*
    fun cargar_libros() {
        try {
            val request = JsonArrayRequest(
                Request.Method.GET,
                config.urlLibros,
                null,
                { response ->
                    try {
                        for (i in 0 until response.length()) {
                            val jsonObject = response.getJSONObject(i)
                            val libro = libro(
                                jsonObject.getInt("id"),
                                jsonObject.getString("titulo"),
                                jsonObject.getString("autor")
                            )
                            libros.add(libro)
                        }
                        libroAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                },
                { error ->
                    error.printStackTrace()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/
    private fun onEditClick(libro: libro) {
        // Implementar lógica de edición
    }

    private fun onDeleteClick(libro: libro) {
        // Implementar lógica de eliminación
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            listaLibroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
