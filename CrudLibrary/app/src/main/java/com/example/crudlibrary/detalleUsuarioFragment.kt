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
import com.example.crudlibrary.models.usuario
import com.google.gson.Gson
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detalleUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class detalleUsuarioFragment : Fragment() {
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

    private lateinit var lblnombre_completo: TextView
    private lateinit var lbldireccion: TextView
    private lateinit var lblcorreo: TextView
    private lateinit var lbltipo_usuario: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar:Button

    //Se asigna un id existente temporal
    private var id_usuario: String=""

    fun consultarUsuario(){
        /*
        Solo se debe consultar, si el ID
        es diferente a vacio
         */

        if (id_usuario != "") {
            var request = JsonObjectRequest(
                Request.Method.GET,//metodo de la peticion
                config.urlUsuarios + id_usuario, //url
                null,//parametros
                { response ->
                    //variable response contiene la respuesta de la API
                    //se convierte de json a un objecto tipo usuario
                    //se genera un objecto de la libreria Gson
                    val gson = Gson()
                    //se conviertr
                    val usuario: usuario = gson.fromJson(response.toString(), usuario::class.java)
                    //se ejecuta el atributo text de los campos con el valor del objeto
                    lblnombre_completo.setText(usuario.nombre_completo)
                    lbldireccion.setText(usuario.direccion)
                    lblcorreo.setText(usuario.correo)
                    lbltipo_usuario.setText(usuario.tipo_usuario)
                    Toast.makeText(context, "Se consulto correctamente", Toast.LENGTH_LONG).show()
                },
                { error ->
                    Toast.makeText(context, "Error al consultar", Toast.LENGTH_LONG).show()
                }//error en la peticon
            )
            var queue = Volley.newRequestQueue(context)
            queue.add(request)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_detalle_usuario, container, false)
        lblnombre_completo = view.findViewById(R.id.lblnombre_completo)
        lbldireccion = view.findViewById(R.id.lbldireccion)
        lblcorreo = view.findViewById(R.id.lblcorreo)
        lbltipo_usuario = view.findViewById(R.id.lbltipo_usuario)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEditar.setOnClickListener {editarUsuario()}
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnEliminar.setOnClickListener{eliminarUsuario()}
        consultarUsuario()
        return view
    }

    fun editarUsuario() {
        var parametros = JSONObject()
        parametros.put("nombre_completo", lblnombre_completo.text.toString())
        parametros.put("direccion", lbldireccion.text.toString())
        parametros.put("correo", lblcorreo.text.toString())
        parametros.put("tipo_usuario", lbltipo_usuario.text.toString())

        var request = JsonObjectRequest(
            Request.Method.PUT, //metodo de la peticion
            config.urlUsuarios + id_usuario, //url
            parametros, //parametros
            { response ->
                Toast.makeText(context, "Se actualizo correctamente", Toast.LENGTH_LONG).show()
            },
            { error ->
                Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
            }//error en la petición
        )
        var queue = Volley.newRequestQueue(context)
        queue.add(request)
    }
    //Se crea el metodo eliminar usuario

    fun eliminarUsuario() {
        if (id_usuario != "") {
            val request = JsonObjectRequest(
                Request.Method.DELETE, // Método DELETE para eliminar el recurso
                config.urlUsuarios + id, // URL del recurso con el ID del usuario a eliminar
                null, // No se envían parámetros en el cuerpo para DELETE
                { response ->
                    Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_LONG).show()
                    // Aquí puedes implementar lógica adicional después de eliminar el libro
                },
                { error ->
                    Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_LONG).show()
                    // Manejo de errores en la petición DELETE
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        } else {
            Toast.makeText(context, "No hay id válido para eliminar el usuario", Toast.LENGTH_LONG).show()
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment detalleUsuarioFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            detalleUsuarioFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}