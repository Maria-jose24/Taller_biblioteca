package com.example.crudlibrary.adapterLibro

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


class adapterLibro (
    var listaLibro: JSONArray,
    var context: Context
): RecyclerView.Adapter<adapterLibro.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val lblTitulo: TextView = itemView.findViewById(R.id.lblTitulo)
        val lblAutor: TextView = itemView.findViewById(R.id.lblAutor)
        val btnEditar: ImageView = itemView.findViewById(R.id.btnEditar)
        val btnEliminar: ImageView = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_libro, parent, false)
        return MyHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val libro = listaLibro.getJSONObject(position)
        holder.lblTitulo.text = libro.getString("titulo")
        holder.lblAutor.text = libro.getString("autor")

        holder.btnEditar.setOnClickListener {
            onclickEditar?.invoke(libro)
        }

        holder.btnEliminar.setOnClickListener {
            onclickEliminar?.invoke(libro)
        }
    }

    override fun getItemCount(): Int {
        return listaLibro.length()
    }

    var onclickEditar: ((JSONObject) -> Unit)? = null
    var onclickEliminar: ((JSONObject) -> Unit)? = null
}