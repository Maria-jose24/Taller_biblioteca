package com.example.bilbioteca.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.bilbioteca.interfaceService.IUsuarioService;
import com.example.bilbioteca.model.Usuario;

@RestController
@RequestMapping("/api/v1/usuario")
@CrossOrigin
public class UsuarioController {


	@Autowired
	private IUsuarioService UsuarioService;

	@PostMapping("/")
	public ResponseEntity<Object> save(@RequestBody Usuario Usuario) {
		List<Usuario> Usuarios = UsuarioService.filtroUsuario(Usuario.getNombre_completo());
		if (!Usuarios.isEmpty()) {
			return new ResponseEntity<>("El nombre completo ya se encuentra registrado", HttpStatus.BAD_REQUEST);
		}

		if (Usuario.getNombre_completo().isEmpty()) {
			return new ResponseEntity<>("El nombre es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (Usuario.getDireccion().isEmpty()) {
			return new ResponseEntity<>("La dirección es obligatoria", HttpStatus.BAD_REQUEST);
		}

		if (Usuario.getCorreo().isEmpty()) {
			return new ResponseEntity<>("El correo electrónico es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (Usuario.getTipo_usuario().isEmpty()) {
			return new ResponseEntity<>("El tipo de usuario es obligatorio", HttpStatus.BAD_REQUEST);
		}

		UsuarioService.save(Usuario);
		return new ResponseEntity<>(Usuario, HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<List<Usuario>> findAll() {
		List<Usuario> listaUsuario = UsuarioService.findAll();
		return new ResponseEntity<>(listaUsuario, HttpStatus.OK);
	}

	@GetMapping("/busquedafiltro/{filtro}")
	public ResponseEntity<List<Usuario>> findFiltro(@PathVariable String filtro) {
		List<Usuario> listaUsuario = UsuarioService.filtroUsuario(filtro);
		return new ResponseEntity<>(listaUsuario, HttpStatus.OK);
	}

	@GetMapping("/{id_usuario}")
	public ResponseEntity<Object> findOne(@PathVariable String id_usuario) {
		Optional<Usuario> usuario = UsuarioService.findOne(id_usuario);
		if (usuario.isPresent()) {
			return new ResponseEntity<>(usuario.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/{id_usuario}")
	public ResponseEntity<Object> update(@PathVariable String id_usuario, @RequestBody Usuario usuarioUpdate) {
		Optional<Usuario> usuarioOptional = UsuarioService.findOne(id_usuario);
		if (!usuarioOptional.isPresent()) {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
		Usuario usuario = usuarioOptional.get();
		if (usuarioUpdate.contieneCamposVacios()) {
			return new ResponseEntity<>("Todos los campos son obligatorios", HttpStatus.BAD_REQUEST);
		}

		usuario.setNombre_completo(usuarioUpdate.getNombre_completo());
		usuario.setDireccion(usuarioUpdate.getDireccion());
		usuario.setCorreo(usuarioUpdate.getCorreo());
		usuario.setTipo_usuario(usuarioUpdate.getTipo_usuario());

		UsuarioService.save(usuario);
		return new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
	}

	@DeleteMapping("/{id_usuario}")
	public ResponseEntity<String> delete(@PathVariable("id_usuario") String id_usuario) {
		if (UsuarioService.findOne(id_usuario).isPresent()) {
			UsuarioService.delete(id_usuario);
			return new ResponseEntity<>("Usuario eliminado", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Usuario no encontrado", HttpStatus.NOT_FOUND);
		}
	}
}
