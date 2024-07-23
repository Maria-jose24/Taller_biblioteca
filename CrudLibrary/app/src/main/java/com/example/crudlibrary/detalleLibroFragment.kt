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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [detalleLibroFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class detalleLibroFragment : Fragment() {
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

    private lateinit var lblTitulo: TextView
    private lateinit var lblAutor: TextView
    private lateinit var lblIsbn: TextView
    private lateinit var lblGenero: TextView
    private lateinit var lblDisponibles: TextView
    private lateinit var lblOcupados: TextView
    private lateinit var btnEditar: Button
    private lateinit var btnEliminar: Button


    //se asigna un id existente temporal
    private var id: String = "cc966008-820e-40b2-90a8-64abc6605c71"

    fun consultarLibro() {
        /*
        solo se debe consultar, si el ID
        es diferente a vacio
         */
        if (id != "") {
            var request = JsonObjectRequest(
                Request.Method.GET,//metodo de la peticion
                config.urlLibros + id, //url
                null,//parametros
                { response ->
                    //variable response contiene la respuesta de la API
                    //se convierte de json a un objecto tipo libro
                    //se genera un objecto de la libreria Gson
                    val gson = Gson()
                    //se conviertr
                    val libro: libro = gson.fromJson(response.toString(), libro::class.java)
                    //se ejecuta el atributo text de los campos con el valor del objeto
                    lblAutor.setText(libro.autor)
                    lblTitulo.setText(libro.titulo)
                    lblIsbn.setText(libro.isbn)
                    lblDisponibles.setText(libro.numero_de_ejemplares_disponibles)
                    lblOcupados.setText(libro.numero_de_ejemplares_ocupados)
                    lblGenero.setText(libro.genero)
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
        val view = inflater.inflate(R.layout.fragment_detalle_libro, container, false)
        lblAutor = view.findViewById(R.id.lblAutor)
        lblTitulo = view.findViewById(R.id.lblTitulo)
        lblIsbn = view.findViewById(R.id.lblIsbn)
        lblDisponibles = view.findViewById(R.id.lblDisponibles)
        lblOcupados = view.findViewById(R.id.lblOcupados)
        lblGenero = view.findViewById(R.id.lblGenero)
        btnEditar = view.findViewById(R.id.btnEditar)
        btnEditar.setOnClickListener {editarLibro()}
        btnEliminar = view.findViewById(R.id.btnEliminar)
        btnEliminar.setOnClickListener{eliminarLibro()}
        consultarLibro()
        return view
    }

    //Se crea el metodo el metodo de editar libro
    fun editarLibro() {
        var parametros = JSONObject()
        parametros.put("titulo", lblTitulo.text.toString())
        parametros.put("autor", lblAutor.text.toString())
        parametros.put("genero", lblGenero.text.toString())
        parametros.put("isbn", lblIsbn.text.toString())
        parametros.put("numero_de_ejemplares_disponibles", lblDisponibles.text.toString())
        parametros.put("numero_de_ejemplares_ocupados", lblOcupados.text.toString())

        var request = JsonObjectRequest(
            Request.Method.PUT, //metodo de la peticion
            config.urlLibros + id, //url
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

    //Se crea el metodo eliminar libro

        fun eliminarLibro() {
            if (id != "") {
                val request = JsonObjectRequest(
                    Request.Method.DELETE, // Método DELETE para eliminar el recurso
                    config.urlLibros + id, // URL del recurso con el ID del libro a eliminar
                    null, // No se envían parámetros en el cuerpo para DELETE
                    { response ->
                        Toast.makeText(context, "Libro eliminado correctamente", Toast.LENGTH_LONG).show()
                        // Aquí puedes implementar lógica adicional después de eliminar el libro
                    },
                    { error ->
                        Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_LONG).show()
                        // Manejo de errores en la petición DELETE
                    }
                )
                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            } else {
                Toast.makeText(context, "No hay id válido para eliminar el libro", Toast.LENGTH_LONG).show()
            }
        }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment detalleLibroFragment.
         */
        // TODO: Rename and change types and number of parameters
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