package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.libro
import com.google.gson.Gson
import org.json.JSONObject

class detalleLibroFragment : Fragment() {

    private lateinit var lblTitulo: TextView
    private lateinit var lblAutor: TextView
    private lateinit var lblIsbn: TextView
    private lateinit var lblGenero: TextView
    private lateinit var lblDisponibles: TextView
    private lateinit var lblOcupados: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button

    private var id: String = ""

    private fun consultarLibro() {
        if (id.isNotEmpty()) {
            val request = JsonObjectRequest(
                Request.Method.GET,
                config.urlLibros + id,
                null,
                { response ->
                    val gson = Gson()
                    val libro = gson.fromJson(response.toString(), libro::class.java)
                    lblAutor.text = libro.autor
                    lblTitulo.text = libro.titulo
                    lblIsbn.text = libro.isbn
                    lblDisponibles.text = libro.numero_de_ejemplares_disponibles
                    lblOcupados.text = libro.numero_de_ejemplares_ocupados
                    lblGenero.text = libro.genero
                    Toast.makeText(context, "Se consultó correctamente", Toast.LENGTH_LONG).show()
                },
                { error ->
                    Toast.makeText(context, "Error al consultar", Toast.LENGTH_LONG).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }
    }

    private fun editarLibro() {
        val parametros = JSONObject().apply {
            put("titulo", lblTitulo.text.toString())
            put("autor", lblAutor.text.toString())
            put("genero", lblGenero.text.toString())
            put("isbn", lblIsbn.text.toString())
            put("numero_de_ejemplares_disponibles", lblDisponibles.text.toString())
            put("numero_de_ejemplares_ocupados", lblOcupados.text.toString())
        }

        val request = JsonObjectRequest(
            Request.Method.PUT,
            config.urlLibros + id,
            parametros,
            { response ->
                Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
            }
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun eliminarLibro() {
        if (id.isNotEmpty()) {
            val request = JsonObjectRequest(
                Request.Method.DELETE,
                config.urlLibros + id,
                null,
                { response ->
                    Toast.makeText(context, "Libro eliminado correctamente", Toast.LENGTH_LONG).show()
                },
                { error ->
                    Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_LONG).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } else {
            Toast.makeText(context, "No hay ID válido para eliminar el libro", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_libro, container, false)
        lblAutor = view.findViewById(R.id.lblAutor)
        lblTitulo = view.findViewById(R.id.lblTitulo)
        lblIsbn = view.findViewById(R.id.lblIsbn)
        lblDisponibles = view.findViewById(R.id.lblDisponibles)
        lblOcupados = view.findViewById(R.id.lblOcupados)
        lblGenero = view.findViewById(R.id.lblGenero)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEditar.setOnClickListener { editarLibro() }
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnEliminar.setOnClickListener { eliminarLibro() }

        arguments?.let {
            id = it.getString(ARG_PARAM1, "")
        }

        consultarLibro()
        return view
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            detalleLibroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
