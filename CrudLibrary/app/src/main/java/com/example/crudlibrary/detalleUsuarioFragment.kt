package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.usuario
import com.google.gson.Gson
import org.json.JSONObject

class detalleUsuarioFragment : Fragment() {

    private var id_usuario: String? = null

    // Definir las variables
    private lateinit var lblnombre_completo: EditText
    private lateinit var lbldireccion: EditText
    private lateinit var lblcorreo: EditText
    private lateinit var lbl_tipo_usuario: Spinner
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnVolver: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            id_usuario = it.getString("id_usuario")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el layout para este fragmento
        val view = inflater.inflate(R.layout.fragment_detalle_usuario, container, false)
        // Inicializar las vistas
        lblnombre_completo = view.findViewById(R.id.lblnombre_completo)
        lbldireccion = view.findViewById(R.id.lbldireccion)
        lblcorreo = view.findViewById(R.id.lblcorreo)
        lbl_tipo_usuario = view.findViewById(R.id.lbl_tipo_usuario)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnVolver = view.findViewById(R.id.imageView2)

        // Configurar el Spinner
        val opciones = arrayOf("Seleccionar...", "Lector", "Bibliotecario", "Administrador")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        lbl_tipo_usuario.adapter = adapter

        // Configurar los botones
        btnEditar.setOnClickListener { editarUsuario() }
        btnEliminar.setOnClickListener { id_usuario?.let { eliminarUsuario(it) } }
        btnVolver.setOnClickListener {
            // Regresa al fragmento de la lista de usuarios
            requireActivity().supportFragmentManager.popBackStack()
        }

        // Consultar el usuario si el ID no es nulo
        id_usuario?.let { consultarUsuario() }

        return view
    }

    private fun consultarUsuario() {
        id_usuario?.let {
            val request = JsonObjectRequest(
                Request.Method.GET,
                "${config.urlUsuarios}$it",
                null,
                { response ->
                    val gson = Gson()
                    val usuario = gson.fromJson(response.toString(), usuario::class.java)
                    lblnombre_completo.setText(usuario.nombre_completo)
                    lbldireccion.setText(usuario.direccion)
                    lblcorreo.setText(usuario.correo)
                    val opciones = arrayOf("Seleccionar...", "Lector", "Bibliotecario", "Administrador")
                    val tipoUsuarioIndex = opciones.indexOf(usuario.tipo_usuario)
                    if (tipoUsuarioIndex >= 0) {
                        lbl_tipo_usuario.setSelection(tipoUsuarioIndex)
                    }
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

    private fun editarUsuario() {
        id_usuario?.let {
            val parametros = JSONObject().apply {
                put("nombre_completo", lblnombre_completo.text.toString())
                put("direccion", lbldireccion.text.toString())
                put("correo", lblcorreo.text.toString())
                put("tipo_usuario", lbl_tipo_usuario.selectedItem.toString())
            }

            val request = JsonObjectRequest(
                Request.Method.PUT,
                "${config.urlUsuarios}$it",
                parametros,
                { response ->
                    Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG).show()
                    parentFragmentManager.popBackStack()
                },
                { error ->
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }
    }

    private fun eliminarUsuario(id_usuario: String) {
        val request = StringRequest(
            Request.Method.DELETE,
            "${config.urlUsuarios}$id_usuario",
            { response ->
                Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_LONG).show()
                parentFragmentManager.popBackStack()
            },
            { error ->
                Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_LONG).show()
            }
        )
        val queue = Volley.newRequestQueue(context)
        queue.add(request)
    }

    companion object {
        @JvmStatic
        fun newInstance(id_usuario: String) =
            detalleUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putString("id_usuario", id_usuario)
                }
            }
    }
}
