package com.example.crudlibrary

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragment_home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragment_home : Fragment() {
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        val texto1 = view.findViewById<TextView>(R.id.texto1)
        val texto2 = view.findViewById<TextView>(R.id.texto2)
        val texto3 = view.findViewById<TextView>(R.id.texto3)

        // Set HTML formatted text
        texto1.text = Html.fromHtml("<b>Juan Pérez:</b> \"La biblioteca tiene una excelente colección de libros. Me encanta venir aquí.\"")
        texto2.text = Html.fromHtml("<b>María García:</b> \"El personal es muy amable y siempre está dispuesto a ayudar.\"")
        texto3.text = Html.fromHtml("<b>Carlos Rodríguez:</b> \"Me gusta mucho la sección de préstamos. Muy eficiente y fácil de usar.\"")

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragment_home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragment_home().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}