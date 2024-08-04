package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.usuario
import com.google.gson.Gson
import org.json.JSONObject

class guardarUsuarioFragment : Fragment() {

    private lateinit var txtnombre_completo: EditText
    private lateinit var txtdireccion: EditText
    private lateinit var txtcorreo: EditText
    private lateinit var spinnerTipoUsuario: Spinner
    private lateinit var btnGuardar: Button
    private lateinit var btnListaUsuarios: Button
    private var id_usuario: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_guardar_usuario, container, false)
        txtnombre_completo = view.findViewById(R.id.txtnombre_completo)
        txtdireccion = view.findViewById(R.id.txtdireccion)
        txtcorreo = view.findViewById(R.id.txtcorreo)
        spinnerTipoUsuario = view.findViewById(R.id.spinner_tipo_usuario)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnListaUsuarios = view.findViewById(R.id.btnListaUsuarios)

        val tiposUsuarios = arrayOf("Lector", "Bibliotecario", "Administrador")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tiposUsuarios)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTipoUsuario.adapter = adapter

        btnGuardar.setOnClickListener { guardarUsuario() }
        btnListaUsuarios.setOnClickListener {
            val fragment = listaUsuarioFragment()
            fragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)
                ?.addToBackStack(null)
                ?.commit()
        }

        return view
    }

    private fun consultarUsuario() {
        if (id_usuario.isNotEmpty()) {
            val request = JsonObjectRequest(
                Request.Method.GET,
                config.urlUsuarios + id_usuario,
                null,
                { response ->
                    val gson = Gson()
                    val usuario = gson.fromJson(response.toString(), usuario::class.java)
                    txtnombre_completo.setText(usuario.nombre_completo)
                    txtdireccion.setText(usuario.direccion)
                    txtcorreo.setText(usuario.correo)
                    val adapter = spinnerTipoUsuario.adapter as ArrayAdapter<String>
                    spinnerTipoUsuario.setSelection(adapter.getPosition(usuario.tipo_usuario))
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

    private fun guardarUsuario() {
        try {
            val parametros = JSONObject().apply {
                put("nombre_completo", txtnombre_completo.text.toString())
                put("direccion", txtdireccion.text.toString())
                put("correo", txtcorreo.text.toString())
                put("tipo_usuario", spinnerTipoUsuario.selectedItem.toString())
            }

            val request = JsonObjectRequest(
                Request.Method.POST,
                config.urlUsuarios,
                parametros,
                { response ->
                    Toast.makeText(context, "Usuario guardado correctamente", Toast.LENGTH_LONG).show()
                    limpiarCampos() // Llamar a la función para limpiar los campos
                },
                { error ->
                    Toast.makeText(context, "Error al guardar Usuario", Toast.LENGTH_LONG).show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Error al guardar el usuario", Toast.LENGTH_LONG).show()
        }
    }

    private fun limpiarCampos() {
        txtnombre_completo.text.clear()
        txtdireccion.text.clear()
        txtcorreo.text.clear()
        spinnerTipoUsuario.setSelection(0) // Restablece el spinner a la opción por defecto
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            guardarUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
