package com.example.crudlibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.crudlibrary.models.libro

class AdapterLibro(private var librosList: List<libro>) : RecyclerView.Adapter<AdapterLibro.LibroViewHolder>() {

    class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.lblTitulo)
        val autor: TextView = itemView.findViewById(R.id.lblAutor)
        val imagen: ImageView = itemView.findViewById(R.id.imageLibro)
        val botonEditar: Button = itemView.findViewById(R.id.buttonEditar)
        val botonEliminar: Button = itemView.findViewById(R.id.buttonEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LibroViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_libro, parent, false)
        return LibroViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LibroViewHolder, position: Int) {
        val libro = librosList[position]
        holder.titulo.text = libro.titulo
        holder.autor.text = libro.autor
        // Aquí puedes cargar la imagen del libro si tienes una URL
        // Glide.with(holder.imagen.context).load(libro.imagenUrl).into(holder.imagen)
        holder.botonEditar.setOnClickListener {
            // Manejar la edición del libro
        }
        holder.botonEliminar.setOnClickListener {
            // Manejar la eliminación del libro
        }
    }

    override fun getItemCount() = librosList.size

    fun actualizarLibros(nuevosLibros: List<libro>) {
        librosList = nuevosLibros
        notifyDataSetChanged()
    }
}
