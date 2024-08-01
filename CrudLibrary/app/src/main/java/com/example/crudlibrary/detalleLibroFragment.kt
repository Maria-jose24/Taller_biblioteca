package com.example.crudlibrary

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
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

private var id: String? =null
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
            id = it.getString("id").toString()
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
    private lateinit var btnVolver: ImageView

    private var id: String = ""

    private fun consultarLibro() {
        id?.let {
            val request = JsonObjectRequest(
                Request.Method.GET,
                config.urlLibros + it,
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
        btnEliminar.setOnClickListener { id?.let { eliminarLibro(it) } }
        btnVolver = view.findViewById(R.id.imageView2)

        btnVolver.setOnClickListener {
            // Regresa al fragmento de la lista de libros
            requireActivity().supportFragmentManager.popBackStack()
        }

        id?.let {
            consultarLibro()
        }
        return view
    }

    private fun editarLibro() {
        id?.let {
            val parametros = JSONObject()
            parametros.put("titulo", lblTitulo.text.toString())
            parametros.put("autor", lblAutor.text.toString())
            parametros.put("genero", lblGenero.text.toString())
            parametros.put("isbn", lblIsbn.text.toString())
            parametros.put("numero_de_ejemplares_disponibles", lblDisponibles.text.toString())
            parametros.put("numero_de_ejemplares_ocupados", lblOcupados.text.toString())

                val request = JsonObjectRequest(
                    Request.Method.PUT,
                    config.urlLibros + it,
                    parametros,
                    { response ->
                        Toast.makeText(context, "Se actualizó correctamente", Toast.LENGTH_LONG)
                            .show()
                    },
                    { error ->
                        Toast.makeText(context, "Error al actualizar", Toast.LENGTH_LONG).show()
                    }
                )
                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            }
        }


            private fun eliminarLibro(id: String) {
                val request = JsonObjectRequest(
                    Request.Method.DELETE,
                    config.urlLibros + id,
                    null,
                    { response ->
                        Toast.makeText(context, "Libro eliminado correctamente", Toast.LENGTH_LONG)
                            .show()
                    },
                    { error ->
                        Toast.makeText(context, "Error al eliminar el libro", Toast.LENGTH_LONG)
                            .show()
                    }
                )
                val queue = Volley.newRequestQueue(context)
                queue.add(request)
            }



        companion object {
            /**vb
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment detalleLibroFragment.
             */
            // TODO: Rename and change types and number of parameters

            @JvmStatic
            fun newInstance(id: String) =
                detalleLibroFragment().apply {
                    arguments = Bundle().apply {
                        putString("id", id)
                    }
                }
        }
    }


