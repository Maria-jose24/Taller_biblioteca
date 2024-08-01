package com.example.crudlibrary

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.adapterLibro.adapterLibro
import com.example.crudlibrary.config.config

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [listaLibroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class listaLibroFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    private lateinit var rootView: View
    private lateinit var editTextBuscar: EditText
    private lateinit var buttonLupa: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.fragment_lista_libro, container, false)
        editTextBuscar = rootView.findViewById(R.id.editTextBuscar)
        buttonLupa = rootView.findViewById(R.id.buttonLupa)

        cargar_libro("")
        setupVolverRegistro()

        buttonLupa.setOnClickListener {
            val filtro = editTextBuscar.text.toString().trim()
            cargar_libro(filtro)
        }

        // Configura el botón de casa, lleva al inicio
        val buttonInicio = rootView.findViewById<Button>(R.id.buttoninicio)
        buttonInicio.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
        }
        return rootView
    }

    private fun setupVolverRegistro() {
        val volverButton = rootView.findViewById<ImageView>(R.id.imageView2)
        volverButton.setOnClickListener {
            requireFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, guardarLibroFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun cargar_libro(filtro: String) {
        val url = if (filtro.isEmpty()) {
            config.urlLibros
        } else {
            "${config.urlLibros}busquedafiltro/$filtro"
        }

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val recycler = rootView.findViewById<RecyclerView>(R.id.listaLibro)
                recycler.layoutManager = LinearLayoutManager(requireContext())
                val adapter = adapterLibro(response, requireContext())

                adapter.onclickEditar = { libro ->
                    val bundle = Bundle().apply {
                        putString("id", libro.getString("id"))
                    }
                    val fragment = detalleLibroFragment().apply {
                        arguments = bundle
                    }
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }

                adapter.onclickEliminar = { libro ->
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("¿Desea eliminar este libro?")
                        .setPositiveButton("Sí") { _, _ ->
                            eliminarLibro(libro.getString("id")) {
                                cargar_libro(filtro) // Actualiza la lista después de eliminar
                            }
                        }
                        .setNegativeButton("No", null)
                        .show()
                }
                recycler.adapter = adapter
            },
            { error ->
                Toast.makeText(context, "Error en la consulta", Toast.LENGTH_LONG).show()
            }
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    private fun eliminarLibro(id: String, onSuccess: () -> Unit) {
        val request = StringRequest(
            Request.Method.DELETE,
            "${config.urlLibros}$id",
            { response ->
                Toast.makeText(context, "Libro eliminado correctamente", Toast.LENGTH_LONG).show()
                onSuccess() // Llama al callback para actualizar la lista
            },
            { error ->
                Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_LONG).show()
            }
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
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
