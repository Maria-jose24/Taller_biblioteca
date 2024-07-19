package com.example.bilbioteca.Controller;

import java.time.LocalDate;

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

import com.example.bilbioteca.interfaceService.IPrestamoService;
import com.example.bilbioteca.model.Prestamo;

@RequestMapping("/api/v1/prestamo")
@RestController
@CrossOrigin
public class PrestamoController {

	@Autowired
	private IPrestamoService PrestamoService;

	@PostMapping("/")
	public ResponseEntity<Object> save(@RequestBody Prestamo prestamo) {

		LocalDate fechaIngreso = LocalDate.parse(prestamo.getFecha_prestamo());
		LocalDate fechaSalida = LocalDate.parse(prestamo.getFecha_devolucion());

		if (fechaSalida.compareTo(fechaIngreso) < 0) {
			return new ResponseEntity<>("La fecha de salida no puede ser anterior a la fecha de ingreso", HttpStatus.BAD_REQUEST);
		}
		if (prestamo.getUsuario().equals("")) {

			return new ResponseEntity<>("El campo usuario es obligatorio", HttpStatus.BAD_REQUEST);
		}
		if (prestamo.getLibro().equals("")) {

			return new ResponseEntity<>("El campo libro es obligatorio", HttpStatus.BAD_REQUEST);
		}
		if (prestamo.getFecha_prestamo().equals("")) {

			return new ResponseEntity<>("El campo fecha de ingreso es obligatorio", HttpStatus.BAD_REQUEST);
		}
		if (prestamo.getFecha_devolucion().equals("")) {

			return new ResponseEntity<>("El campo fecha de salida es obligatorio", HttpStatus.BAD_REQUEST);
		}
		if (prestamo.getEstado_prestamo().equals("")) {

			return new ResponseEntity<>("El campo estado es obligatorio", HttpStatus.BAD_REQUEST);
		} 

		PrestamoService.save(prestamo);

		return new ResponseEntity<>(prestamo, HttpStatus.OK);

	}

	@GetMapping("/")
	public ResponseEntity<Object> findAll() {
		var listaPrestamo = PrestamoService.findAll();
		return new ResponseEntity<>(listaPrestamo, HttpStatus.OK);
	}

	@GetMapping("/{id_prestamo}")
	public ResponseEntity<Object> findOne(@PathVariable String id_prestamo) {
		var prestamo = PrestamoService.findOne(id_prestamo);
		return new ResponseEntity<>(prestamo, HttpStatus.OK);
	}
	@PutMapping("/{id_prestamo}")
	public ResponseEntity<Object> update(@PathVariable String id_prestamo, @RequestBody Prestamo prestamoUpdate) {
		if (prestamoUpdate.contieneCamposVacios()) {
			return new ResponseEntity<>("Todos los campos son obligatorios", HttpStatus.BAD_REQUEST);
		}
		var prestamo= PrestamoService.findOne(id_prestamo).get();
		if (prestamo != null){
			
			
			LocalDate fechaIngreso = LocalDate.parse(prestamoUpdate.getFecha_prestamo());
			LocalDate fechaSalida = LocalDate.parse(prestamoUpdate.getFecha_devolucion());

			if (fechaSalida.compareTo(fechaIngreso) < 0) {
				return new ResponseEntity<>("La fecha de salida no puede ser anterior a la fecha de ingreso", HttpStatus.BAD_REQUEST);
			}


			prestamo.setUsuario(prestamoUpdate.getUsuario());
			prestamo.setLibro(prestamoUpdate.getLibro());
			prestamo.setFecha_prestamo(prestamoUpdate.getFecha_prestamo());
			prestamo.setFecha_devolucion(prestamoUpdate.getFecha_devolucion());
			prestamo.setEstado_prestamo(prestamoUpdate.getEstado_prestamo());

			PrestamoService.save(prestamo);
			return new ResponseEntity<>("Guardado", HttpStatus.OK);

		}
		else {
			return new ResponseEntity<>("Error prestamo no encontrado", HttpStatus.BAD_REQUEST);
		}
	}


	@DeleteMapping("/{id_prestamo}")
	public ResponseEntity<Object>delete (@PathVariable("id_prestamo")String id_prestamo){
		PrestamoService.delete(id_prestamo);
		return new ResponseEntity<>("Prestamo eliminado",HttpStatus.OK);
	}
}