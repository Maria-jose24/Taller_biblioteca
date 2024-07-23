package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.config
import com.example.crudlibrary.models.usuario
import com.google.gson.Gson
import org.json.JSONObject
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [guardarUsuarioFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class guardarUsuarioFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //Se define las variables del formulario
    private lateinit var txtnombre_completo: EditText
    private lateinit var txtdireccion: EditText
    private lateinit var txtcorreo: EditText
    private lateinit var txttipo_usuario: EditText
    private lateinit var btnGuardar: Button

    //Se asigna un id existente temporal
    private var id_usuario: String=""

    /*
       Request es la peticion que se le hace a la API
       StringRequest=responde un String
       JsonRequest=responde un Json
       JsonArrayRequest=responde un arreglo de Json
        */

    /*
    metodo encargado de traer la informacion del usuario
     */
    fun consultarUsuario(){
        /*
        solo se debe consultar, si el ID
        es diferente a vacio
         */
        if (id_usuario !=""){
            var request= JsonObjectRequest(
                Method.GET,//metodo de la peticion
                config.urlUsuarios+id_usuario, //url
                null,//parametros
                {response->
                    //variable response contiene la respuesta de la API
                    //se convierte de json a un objecto tipo usuario
                    //se genera un objecto de la libreria Gson
                    val gson = Gson()
                    //se convierte
                    val usuario: usuario =gson.fromJson(response.toString(), usuario::class.java)
                    //se ejecuta el atributo text de los campos con el valor del objeto
                    txtnombre_completo.setText(usuario.nombre_completo)
                    txtdireccion.setText(usuario.direccion)
                    txtcorreo.setText(usuario.correo)
                    txttipo_usuario.setText(usuario.tipo_usuario)
                    Toast.makeText(context, "Se consulto correctamente", Toast.LENGTH_LONG).show()

                },
                {error->
                    Toast.makeText(context,"Error al consultar", Toast.LENGTH_LONG).show()
                }//error en la peticon
            )
            var queue= Volley.newRequestQueue(context)
            queue.add(request)
        }
    }
    fun guardarUsuario(){
        try {
            if (id_usuario==""){//se crea el usuario

                var parametros = JSONObject()
                parametros.put("nombre_completo", txtnombre_completo.text.toString())
                parametros.put("direccion", txtdireccion.text.toString())
                parametros.put("correo", txtcorreo.text.toString())
                parametros.put("tipo_usuario", txttipo_usuario.text.toString())

                //uno por cada data que se requiere
                var  request= JsonObjectRequest(
                    Request.Method.POST,//metodo
                    config.urlUsuarios,//url
                    parametros,
                    {response->
                        Toast.makeText(context,"Se guardo correctamente", Toast.LENGTH_LONG).show()
                    },//cuando la respuesta es correcta
                    {error->
                        Toast.makeText(context,"Se genero un error", Toast.LENGTH_LONG).show()
                    }//cuando es incorrecto
                )
                //se crea la cola de tabajo
                val queue= Volley.newRequestQueue(context)
                //se añade la peticion
                queue.add(request)
            }else{//se actualiza el usuario
                //parametros
                var parametros= JSONObject()
                parametros.put("nombre_completo", txtnombre_completo.text.toString())
                parametros.put("direccion", txtdireccion.text.toString())
                parametros.put("correo", txtcorreo.text.toString())
                parametros.put("tipo_usuario", txttipo_usuario.text.toString())

                var request= JsonObjectRequest(
                    Method.PUT,//metodo de la peticion
                    config.urlUsuarios+id_usuario, //url
                    parametros,//parametros
                    {response->

                        Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG).show()
                    },
                    {error->
                        Toast.makeText(context,"Error al actualizar", Toast.LENGTH_LONG).show()
                    }//error en la peticon
                )
                var queue= Volley.newRequestQueue(context)
                queue.add(request)
            }
        }catch (error: Exception){

        }
    }


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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_guardar_usuario, container, false)
        txtnombre_completo = view.findViewById(R.id.txtnombre_completo)
        txtdireccion = view.findViewById(R.id.txtdireccion)
        txtcorreo = view.findViewById(R.id.txtcorreo)
        txttipo_usuario = view.findViewById(R.id.txttipo_usuario)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener {
            guardarUsuario()
        }
        consultarUsuario()
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment guardarUsuarioFragment.
         */
        // TODO: Rename and change types and number of parameters
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