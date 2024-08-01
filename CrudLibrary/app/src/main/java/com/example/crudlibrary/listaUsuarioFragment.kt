package com.example.crudlibrary

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.adapterLibro.adapterLibro
import com.example.crudlibrary.adapterUsuario.adapterUsuario
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.usuario


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [listaUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class listaUsuarioFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        rootView = inflater.inflate(R.layout.fragment_lista_usuario, container, false)
        editTextBuscar = rootView.findViewById(R.id.editTextBuscar)
        buttonLupa = rootView.findViewById(R.id.buttonLupa)

        cargar_usuario("")
        setupVolverRegistro()

        buttonLupa.setOnClickListener {
            val filtro = editTextBuscar.text.toString().trim()
            cargar_usuario(filtro)
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
                .replace(R.id.fragment_container, guardarUsuarioFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun cargar_usuario(filtro: String) {
        val url = if (filtro.isEmpty()) {
            config.urlUsuarios
        } else {
            "${config.urlUsuarios}busquedafiltro/$filtro"
        }

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val recycler = rootView.findViewById<RecyclerView>(R.id.listaUsuario)
                recycler.layoutManager = LinearLayoutManager(requireContext())
                val adapter = adapterUsuario(response, requireContext()) // Cambiado a adapterUsuario

                adapter.onclickEditar = { usuario ->
                    val bundle = Bundle().apply {
                        putString("id_usuario", usuario.getString("id_usuario"))
                    }
                    val fragment = detalleUsuarioFragment().apply {
                        arguments = bundle
                    }
                    requireFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }


                adapter.onclickEliminar = { usuario ->
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setMessage("¿Desea eliminar este usuario?") // Cambiado a eliminar usuario
                        .setPositiveButton("Sí") { _, _ ->
                            eliminarUsuario(usuario.getString("id_usuario")) {
                                cargar_usuario(filtro) // Actualiza la lista después de eliminar
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
    private fun eliminarUsuario(id_usuario: String, onSuccess: () -> Unit) {
        val request = StringRequest(
            Request.Method.DELETE,
            "${config.urlUsuarios}$id_usuario",
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
            listaUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
