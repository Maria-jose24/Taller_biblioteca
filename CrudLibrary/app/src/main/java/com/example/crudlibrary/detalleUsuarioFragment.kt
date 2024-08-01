package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.usuario
import com.google.gson.Gson
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private var id_usuario: String? =null


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
            id_usuario = it.getString("id_usuario").toString()
        }
    }

    private lateinit var lblnombre_completo: TextView
    private lateinit var lbldireccion: TextView
    private lateinit var lblcorreo: TextView
    private lateinit var spinnerTipoUsuario: Spinner
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button
    private lateinit var btnVolver: ImageView

    //Se asigna un id existente temporal
    private var id_usuario: String = ""

    fun consultarUsuario() {
        id_usuario?.let{
            val request = JsonObjectRequest(
                Request.Method.GET,
                config.urlUsuarios + it,
                null,//parametros
                { response ->
                    val gson = Gson()
                    val usuario = gson.fromJson(response.toString(), usuario::class.java)
                    lblnombre_completo.text = usuario.nombre_completo
                    lbldireccion.text = usuario.direccion
                    lblcorreo.text = usuario.correo
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detalle_usuario, container, false)
        lblnombre_completo = view.findViewById(R.id.lblnombre_completo)
        lbldireccion = view.findViewById(R.id.lbldireccion)
        lblcorreo = view.findViewById(R.id.lblcorreo)
        spinnerTipoUsuario = view.findViewById(R.id.spinner_tipo_usuario)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEditar.setOnClickListener { editarUsuario() }
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnEliminar.setOnClickListener { id_usuario?.let { eliminarUsuario(it) } }
        btnVolver = view.findViewById(R.id.imageView2)

        btnVolver.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        id_usuario.let {
            consultarUsuario()
        }
        return view
    }

    fun editarUsuario() {
        id_usuario?.let {
            val parametros = JSONObject()
            parametros.put("nombre_completo", lblnombre_completo.text.toString())
            parametros.put("direccion", lbldireccion.text.toString())
            parametros.put("correo", lblcorreo.text.toString())
            parametros.put("tipo_usuario", spinnerTipoUsuario.selectedItem.toString())

            val request = JsonObjectRequest(
                Request.Method.PUT, //metodo de la peticion
                config.urlUsuarios + it, //url
                parametros, //parametros
                { response ->
                    Toast.makeText(context, response.getString("respuesta"), Toast.LENGTH_LONG)
                        .show()
                },
                { error ->
                    Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
                }//error en la petición
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
        }
    }
    //Se crea el metodo eliminar usuario

    fun eliminarUsuario(id_usuario: String) {
            val request = JsonObjectRequest(
                Request.Method.DELETE, // Método DELETE para eliminar el recurso
                config.urlUsuarios + id_usuario, // URL del recurso con el ID del usuario a eliminar
                null, // No se envían parámetros en el cuerpo para DELETE
                { response ->
                    Toast.makeText(context, "Usuario eliminado correctamente", Toast.LENGTH_LONG)
                        .show()
                    // Aquí puedes implementar lógica adicional después de eliminar el libro
                },
                { error ->
                    Toast.makeText(context, "Error al eliminar el usuario", Toast.LENGTH_LONG)
                        .show()
                }
            )
            val queue = Volley.newRequestQueue(context)
            queue.add(request)
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
        fun newInstance(id_usuario: String) =
            detalleUsuarioFragment().apply {
                arguments = Bundle().apply {
                   putString("id_usuario", id_usuario)
                }
            }
    }
}
