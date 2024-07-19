package com.example.bilbioteca.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.bilbioteca.model.Libro;

@Repository

public interface ILibro extends CrudRepository <Libro,String>{


	@Query("SELECT l FROM Libro l WHERE "
			+ "l.titulo LIKE %?1% OR "
			+ "l.autor LIKE %?1% OR "
			+ "l.isbn LIKE %?1% OR "
			+ "l.genero LIKE %?1%")
	List<Libro> filtroLibro(String filtro);

	@Query("SELECT l FROM Libro l WHERE l.titulo = ?1")
	List<Libro> filtroIngresoLibro(String titulo);

	@Query("SELECT l FROM Libro l WHERE l.isbn = ?1")
	Optional<Libro> findByIsbn(String isbn);
	
	@Query("SELECT l FROM Libro l WHERE l.titulo = ?1")
	List<Libro> findByTitulo(String titulo);

}
