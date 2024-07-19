package com.example.bilbioteca.interfaceService;

import java.util.List;
import java.util.Optional;

import com.example.bilbioteca.model.Usuario;

public interface IUsuarioService {

	String save(Usuario usuario);
	List<Usuario> findAll();
	List<Usuario> filtroUsuario(String filtro);
	Optional<Usuario> findOne(String id);
	void delete(String id);
}

