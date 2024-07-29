package com.example.crudlibrary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.crudlibrary.models.libro

class AdapterLibro(
    private var librosList: List<libro>,
    private val onEditClick: (libro) -> Unit,
    private val onDeleteClick: (libro) -> Unit
) : RecyclerView.Adapter<AdapterLibro.LibroViewHolder>() {

    class LibroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.lblTitulo)
        val autor: TextView = itemView.findViewById(R.id.lblAutor)
        val genero: TextView = itemView.findViewById(R.id.lblGenero)
        val isbn: TextView = itemView.findViewById(R.id.lblIsbn)
        val disponibles: TextView = itemView.findViewById(R.id.lblDisponibles)
        val ocupados: TextView = itemView.findViewById(R.id.lblOcupados)
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
        holder.genero.text = libro.genero
        holder.isbn.text = libro.isbn
        holder.disponibles.text = libro.numero_de_ejemplares_disponibles
        holder.ocupados.text = libro.numero_de_ejemplares_ocupados

        // Cargar la imagen del libro usando Glide si se tiene una URL
        // Glide.with(holder.imagen.context).load(libro.imagenUrl).into(holder.imagen)

        holder.botonEditar.setOnClickListener {
            onEditClick(libro)
        }
        holder.botonEliminar.setOnClickListener {
            onDeleteClick(libro)
        }
    }

    override fun getItemCount() = librosList.size

    fun actualizarLibros(nuevosLibros: List<libro>) {
        librosList = nuevosLibros
        notifyDataSetChanged()
    }
}
