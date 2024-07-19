package com.example.bilbioteca.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bilbioteca.interfaceService.IPrestamoService;
import com.example.bilbioteca.interfaces.IPrestamo;
import com.example.bilbioteca.model.Prestamo;

@Service
public class PrestamoService implements IPrestamoService{


	@Autowired 
	private IPrestamo data;

	@Override
	public String save(Prestamo prestamo) {
		data.save(prestamo);
		return prestamo.getId_prestamo();
	}

	@Override
	public List<Prestamo> findAll() {
		List <Prestamo> listaPrestamo = (List<Prestamo>) data.findAll();
		return listaPrestamo;
	}


	@Override
	public Optional<Prestamo> findOne(String id_prestamo) {
		Optional<Prestamo>prestamo=data.findById(id_prestamo);

		return prestamo;
	}

	@Override
	public int delete(String id_prestamo) {
		data.deleteById(id_prestamo);
		return 1;
	}
}
