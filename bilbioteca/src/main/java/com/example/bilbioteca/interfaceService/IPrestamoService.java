package com.example.bilbioteca.interfaceService;

import java.util.List;
import java.util.Optional;

import com.example.bilbioteca.model.Prestamo;

public interface IPrestamoService {
	
    public String save(Prestamo prestamo);

    public List<Prestamo> findAll();

    public Optional<Prestamo> findOne(String id);

    public int delete(String id);
}
