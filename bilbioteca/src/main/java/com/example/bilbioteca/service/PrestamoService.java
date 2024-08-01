package com.example.bilbioteca.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.bilbioteca.interfaceService.ILibroService;
import com.example.bilbioteca.interfaceService.IPrestamoService;
import com.example.bilbioteca.interfaceService.IUsuarioService;
import com.example.bilbioteca.interfaces.ILibro;
import com.example.bilbioteca.interfaces.IPrestamo;
import com.example.bilbioteca.interfaces.IUsuario;
import com.example.bilbioteca.model.Libro;
import com.example.bilbioteca.model.Prestamo;
import com.example.bilbioteca.model.Usuario;

@Service
public class PrestamoService implements IPrestamoService{


	@Autowired 
	private IPrestamo data;
	
	@Autowired
	private IUsuario UsuarioService;

	@Autowired
	private ILibro LibroService;

	@Override
	public String save(Prestamo prestamo) {
		Optional<Usuario>Usuario = UsuarioService.findById(prestamo.getUsuario().getId_usuario());
		Optional<Libro>Libro = LibroService.findById(prestamo.getLibro().getId());
		
		if (!Usuario.isPresent()) {
			throw new IllegalArgumentException("Usuario no encontrado");
		}
		if (!Libro.isPresent()) {
			throw new IllegalArgumentException("Libro no encontrado");
		}
		
		prestamo.setUsuario(Usuario.get());
		prestamo.setLibro(Libro.get());
		
		return data.save(prestamo).getId_prestamo();
	}

	@Override
	public List<Prestamo> findAll() {
		return (List<Prestamo>)data.findAll();
	}
	

	@Override
	public Optional<Prestamo> findOne(String id_prestamo) {
		return data.findById(id_prestamo);
	}

	@Override
	public int delete(String id_prestamo) {
		data.deleteById(id_prestamo);
		return 1;
	}
}

