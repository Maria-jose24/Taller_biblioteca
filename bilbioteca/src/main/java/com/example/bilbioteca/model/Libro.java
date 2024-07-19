package com.example.bilbioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="Libro")
public class Libro {


	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name="id",nullable=false,length=36)
	private String id;


	@Column (name="titulo",nullable=false,length=40)
	private String titulo;


	@Column (name="autor",nullable=false,length=40)
	private String autor;

	@Column (name="isbn",nullable=false,length=13)
	private String isbn;

	@Column (name="genero",nullable=false,length=40)
	private String genero;

	@Column (name="numero_de_ejemplares_disponibles",nullable=false,length=40)
	private String numero_de_ejemplares_disponibles;

	@Column (name="numero_de_ejemplares_ocupados",nullable=false,length=40)
	private String numero_de_ejemplares_ocupados;



	public Libro() {
		super();
	}

	public Libro(String id, String titulo, String autor, String isbn, String genero,
			String numero_de_ejemplares_disponibles, String numero_de_ejemplares_ocupados) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.isbn = isbn;
		this.genero = genero;
		this.numero_de_ejemplares_disponibles = numero_de_ejemplares_disponibles;
		this.numero_de_ejemplares_ocupados = numero_de_ejemplares_ocupados;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getNumero_de_ejemplares_disponibles() {
		return numero_de_ejemplares_disponibles;
	}

	public void setNumero_de_ejemplares_disponibles(String numero_de_ejemplares_disponibles) {
		this.numero_de_ejemplares_disponibles = numero_de_ejemplares_disponibles;
	}

	public String getNumero_de_ejemplares_ocupados() {
		return numero_de_ejemplares_ocupados;
	}

	public void setNumero_de_ejemplares_ocupados(String numero_de_ejemplares_ocupados) {
		this.numero_de_ejemplares_ocupados = numero_de_ejemplares_ocupados;
	}
	public boolean contieneCamposVacios() {
		return titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || genero.isEmpty() || 
				numero_de_ejemplares_disponibles.isEmpty() || numero_de_ejemplares_ocupados.isEmpty();
	}
}