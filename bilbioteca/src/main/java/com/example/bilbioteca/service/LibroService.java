package com.example.bilbioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bilbioteca.interfaceService.ILibroService;
import com.example.bilbioteca.interfaces.ILibro;
import com.example.bilbioteca.model.Libro;

@Service
public class LibroService implements ILibroService{

	@Autowired 
	private ILibro data;

	@Override
	public String save(Libro Libro) {
		data.save(Libro);
		return Libro.getId();
	}

	@Override
	public List<Libro> findAll() {
		List <Libro> listaLibro = (List<Libro>) data.findAll() ;
		return listaLibro;
	}

	@Override
	public List<Libro> filtroLibro(String filtro) {
		List <Libro> listaLibro=data.filtroLibro(filtro);
		return listaLibro;
	}


	@Override
	public Optional<Libro> findOne(String id) {
		Optional<Libro>Libro=data.findById(id);
		return Libro;
	}


	@Override
	public int delete(String id) {
		data.deleteById(id);
		return 1;
	}

	@Override
	public List<Libro> filtroIngresoLibro(String titulo) {
		List<Libro> listaLibro=data.filtroIngresoLibro(titulo);
		return listaLibro;
	}
	@Override
	public Optional<Libro> findByIsbn(String isbn) {
		return data.findByIsbn(isbn);
	}

	@Override
	public List<Libro> findByTitulo(String titulo) {
		return data.findByTitulo(titulo);
	}

}
