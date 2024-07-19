package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Request.Method
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.JsonRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.crudlibrary.config.*
import com.example.crudlibrary.models.libro
import com.google.gson.Gson
//import com.google.gson.JsonObject//genera error
import  org.json.JSONObject
import java.lang.Exception

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [guardarLibroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class guardarLibroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    //se definen las varibales del formulario
    private lateinit var txtTitulo: EditText
    private  lateinit var txtAutor: EditText
    private lateinit var txtGenero: EditText
    private  lateinit var  txtIsbn: EditText
    private  lateinit var txtDisponible: EditText
    private  lateinit var  txtOcupado: EditText
    private  lateinit var btnGuardar:Button
    private  var id:String="138d1805-8495-4691-9c9c-adec706edef1"

    /*
    Request es la peticion que se le hace a la API
    StringRequest=responde un String
    JsonRequest=responde un Json
    JsonArrayRequest=responde un arreglo de Json
     */


    /*
    metodo encargado de traer la informacion del libro
     */
    fun consultarLibro(){
        /*
        solo se debe consultar, si el ID
        es diferente a vacio
         */
        if (id !=""){
            var request=JsonObjectRequest(
                Method.GET,//metodo de la peticion
                config.urlLibros+id, //url
                null,//parametros
                {response->
                    //variable response contiene la respuesta de la API
                    //se convierte de json a un objecto tipo libro
                    //se genera un objecto de la libreria Gson
                    val gson = Gson()
                    //se conviertr
                    val libro:libro=gson.fromJson(response.toString(),libro::class.java)
                    //se ejecuta el atributo text de los campos con el valor del objeto
                    txtAutor.setText(libro.autor)
                    txtTitulo.setText(libro.titulo)
                    txtIsbn.setText(libro.isbn)
                    txtDisponible.setText(libro.numero_de_ejemplares_disponibles)
                    txtOcupado.setText(libro.numero_de_ejemplares_ocupados)
                    txtGenero.setText(libro.genero)
                    Toast.makeText(context, "Se consulto correctamente", Toast.LENGTH_LONG).show()

                },
                {error->
                    Toast.makeText(context,"Error al consultar",Toast.LENGTH_LONG).show()
                }//error en la peticon
            )
            var queue=Volley.newRequestQueue(context)
            queue.add(request)
        }
    }
    fun guardarLibro(){
        try {
            if (id==""){//se crea el libro
                /*
                //se crea la peticion
                ejemplo que recibe un peticion String
                val request=object: StringRequest(
                    Request.Method.POST,//metodo de la peticion
                    config.urlbBase,// url de la peticion
                    Response.Listener {
                        //metodo cuando se ejecuta cuando la peticion es correcta
                        Toast.makeText(context,"se guarda correctamente", Toast.LENGTH_LONG).show()
                    },
                    Response.ErrorListener {
                        //metodo cuando se ejecuta cuando la peticion genera error
                        Toast.makeText(context,"Error al guardar", Toast.LENGTH_LONG).show()
                    }
                )
                {
                    override fun getParams(): Map<String,String> {
                        var parametros=HashMap<String,String>()
                        parametros.put("titulo",txtTitulo.text.toString())
                        parametros.put("autor",txtAutor.text.toString())
                        parametros.put("genero",txtGenero.text.toString())
                        parametros.put("isbn",txtIsbn.text.toString())
                        parametros.put("numero_ejemplares_disponibles",txtDisponible.text.toString())
                        parametros.put("numero_ejemplares_ocupados",txtOcupado.text.toString())
                        //uno por cada data que se requiere
                        return parametros
                    }
                }
                */

                var parametros=JSONObject()
                parametros.put("titulo",txtTitulo.text.toString())
                parametros.put("autor",txtAutor.text.toString())
                parametros.put("genero",txtGenero.text.toString())
                parametros.put("isbn",txtIsbn.text.toString())
                parametros.put("numero_de_ejemplares_disponibles",txtDisponible.text.toString())
                parametros.put("numero_de_ejemplares_ocupados",txtOcupado.text.toString())
                //uno por cada data que se requiere
                var  request=JsonObjectRequest(
                    Request.Method.POST,//metodo
                    config.urlLibros,//url
                    parametros,
                    {response->
                        Toast.makeText(context,"Se guardo correctamente",Toast.LENGTH_LONG).show()
                    },//cuando la respuesta es correcta
                    {error->
                        Toast.makeText(context,"Se genero un error",Toast.LENGTH_LONG).show()
                    }//cuando es incorrecto
                )
                //se crea la cola de tabajo
                val queue=Volley.newRequestQueue(context)
                //se añade la peticion
                queue.add(request)
            }else{//se actualiza el libro
                //parametros
                var parametros=JSONObject()
                parametros.put("titulo",txtTitulo.text.toString())
                parametros.put("autor",txtAutor.text.toString())
                parametros.put("genero",txtGenero.text.toString())
                parametros.put("isbn",txtIsbn.text.toString())
                parametros.put("numero_de_ejemplares_disponibles",txtDisponible.text.toString())
                parametros.put("numero_de_ejemplares_ocupados",txtOcupado.text.toString())

                var request=JsonObjectRequest(
                    Method.PUT,//metodo de la peticion
                    config.urlLibros+id, //url
                    parametros,//parametros
                    {response->
                        //variable response contiene la respuesta de la API
                        //se convierte de json a un objecto tipo libro
                        //se genera un objecto de la libreria Gson
                        /*val gson = Gson()
                        //se conviertr
                        val libro:libro=gson.fromJson(response.toString(),libro::class.java)
                        //se ejecuta el atributo text de los campos con el valor del objeto
                        txtAutor.setText(libro.autor)
                        txtTitulo.setText(libro.titulo)
                        txtIsbn.setText(libro.isbn)
                        txtDisponible.setText(libro.numero_de_ejemplares_disponibles)
                        txtOcupado.setText(libro.numero_de_ejemplares_ocupados)
                        txtGenero.setText(libro.genero)

                         */
                        Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG).show()
                    },
                    {error->
                        Toast.makeText(context,"Error al actualizar",Toast.LENGTH_LONG).show()
                    }//error en la peticon
                )
                var queue=Volley.newRequestQueue(context)
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
        var view= inflater.inflate(R.layout.fragment_guardar_libro, container, false)
        txtTitulo=view.findViewById(R.id.txtTitulo)
        txtAutor=view.findViewById(R.id.txtAutor)
        txtGenero=view.findViewById(R.id.txtGenero)
        txtIsbn=view.findViewById(R.id.txtIsbn)
        txtDisponible=view.findViewById(R.id.txtDisponibles)
        txtOcupado=view.findViewById(R.id.txtOcupados)
        btnGuardar=view.findViewById(R.id.btnGuardar)
        btnGuardar.setOnClickListener{
            guardarLibro()
        }
        consultarLibro()
        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment guardarLibroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            guardarLibroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}