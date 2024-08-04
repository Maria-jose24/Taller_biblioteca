package com.example.crudlibrary.adapterUsuario

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crudlibrary.R
import org.json.JSONArray
import org.json.JSONObject

class adapterUsuario (
    var listaUsuario: JSONArray,
    var context: Context
): RecyclerView.Adapter<adapterUsuario.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblnombre_completo: TextView = itemView.findViewById(R.id.lblnombre_completo)
        val spinner_tipo_usuario: TextView = itemView.findViewById(R.id.spinner_tipo_usuario)
        val btnEditar: ImageView = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_usuario, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val usuario = listaUsuario.getJSONObject(position)
        holder.lblnombre_completo.text = usuario.getString("nombre_completo")
        holder.spinner_tipo_usuario.text = usuario.getString("tipo_usuario")

        holder.btnEditar.setOnClickListener {
            onclickEditar?.invoke(usuario)
        }

        holder.btnEliminar.setOnClickListener {
            onclickEliminar?.invoke(usuario)
        }
    }

    override fun getItemCount(): Int {
        return listaUsuario.length()
    }

    var onclickEditar: ((JSONObject) -> Unit)? = null
    var onclickEliminar: ((JSONObject) -> Unit)? = null
}
