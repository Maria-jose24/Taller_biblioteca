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

import com.example.bilbioteca.interfaceService.ILibroService;
import com.example.bilbioteca.model.Libro;
import com.example.bilbioteca.model.respuesta;

@RestController
@RequestMapping("/api/v1/libro")
@CrossOrigin
public class LibroController {

	@Autowired
	private ILibroService LibroService;

	@PostMapping("/")
	public ResponseEntity<Object> save(@RequestBody Libro libro) {

		List<Libro> libros = LibroService.filtroIngresoLibro(libro.getTitulo());
		if (!libros.isEmpty()) {
			return new ResponseEntity<>("El libro ya se encuentra registrado", HttpStatus.BAD_REQUEST);
		}
		if (libro.getTitulo().equals("")) {

			return new ResponseEntity<>("El titulo del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getAutor().equals("")) {

			return new ResponseEntity<>("El autor del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if(libro.getIsbn().length()!=13) {
			return new ResponseEntity<>("Error, son solamente 13 numeros",HttpStatus.BAD_REQUEST);
		}
		if ( libro.getIsbn().equals("")) {

			return new ResponseEntity<>("El ISBN del libro es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getNumero_de_ejemplares_disponibles().equals("")) {

			return new ResponseEntity<>("El numero de ejemplares disponibles es obligatorio", HttpStatus.BAD_REQUEST);
		}

		if (libro.getNumero_de_ejemplares_ocupados().equals("")) {

			return new ResponseEntity<>("El numero de ejemplares ocupados es obligatorio", HttpStatus.BAD_REQUEST);
		}

		LibroService.save(libro);
		return new ResponseEntity<>(libro,HttpStatus.OK);
	}

	@GetMapping("/")
	public ResponseEntity<Object>findAll(){
		var ListaLibro = LibroService.findAll();
		return new ResponseEntity<>(ListaLibro, HttpStatus.OK);
	}

	//filtro
	@GetMapping("/busquedafiltro/{filtro}")
	public ResponseEntity<Object>findFiltro(@PathVariable String filtro){
		var ListaLibro = LibroService.filtroLibro(filtro);
		return new ResponseEntity<>(ListaLibro, HttpStatus.OK);
	}
	//@PathVariable recibe una variable por el enlace

	@GetMapping("/{id}")
	public ResponseEntity<Object> findOne ( @PathVariable String id){
		var libro= LibroService.findOne(id);
		return new ResponseEntity<>(libro, HttpStatus.OK);
	}


	@PutMapping("/{id}")
	public ResponseEntity<respuesta> update(@PathVariable String id, @RequestBody Libro libroUpdate) {
		respuesta respuesta = new respuesta();
		if (libroUpdate.contieneCamposVacios()) {
			respuesta.setEstado("Error");
			respuesta.setRespuesta("Todos los campos son obligatorios");
			return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
		}

		Optional<Libro> optionalLibro = LibroService.findOne(id);
		if (optionalLibro.isPresent()) {
			Libro libro = optionalLibro.get();

			if (!libro.getIsbn().equals(libroUpdate.getIsbn())) {
				Optional<Libro> existingLibro = LibroService.findByIsbn(libroUpdate.getIsbn());
				if (existingLibro.isPresent()) {
					respuesta.setEstado("Error");
					respuesta.setRespuesta("Ya existe un libro con el mismo ISBN");
					return new ResponseEntity<>(respuesta, HttpStatus.BAD_REQUEST);
				}
			}

			libro.setTitulo(libroUpdate.getTitulo());
			libro.setAutor(libroUpdate.getAutor());
			libro.setIsbn(libroUpdate.getIsbn());
			libro.setGenero(libroUpdate.getGenero());
			libro.setNumero_de_ejemplares_disponibles(libroUpdate.getNumero_de_ejemplares_disponibles());
			libro.setNumero_de_ejemplares_ocupados(libroUpdate.getNumero_de_ejemplares_ocupados());

			LibroService.save(libro);
			respuesta.setEstado("Success");
			respuesta.setRespuesta("Libro actualizado correctamente");
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} else {
			respuesta.setEstado("Error");
			respuesta.setRespuesta("Libro no encontrado");
			return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<respuesta> delete(@PathVariable("id") String id) {
		respuesta respuesta = new respuesta();
		int result = LibroService.delete(id);
		if (result == 1) {
			respuesta.setEstado("Success");
			respuesta.setRespuesta("Libro eliminado correctamente");
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		} else {
			respuesta.setEstado("Error");
			respuesta.setRespuesta("Libro no encontrado");
			return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
		}
	}
}