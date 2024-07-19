package com.example.bilbioteca.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name="Usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	@Column(name="id_usuario",nullable=false,length=36)
	private String id_usuario;


	@Column(name="nombre_completo",nullable=false,length=100)
	private String nombre_completo;

	@Column(name="direccion",nullable=false,length=40)
	private String direccion;

	@Column(name="correo",nullable=false,length=40)
	private String correo;

	@Column(name="tipo_usuario",nullable=false,length=40)
	private String tipo_usuario;

	public Usuario() {
		super();
	}

	public Usuario(String id_usuario, String nombre_completo, String direccion, String correo, String tipo_usuario) {
		super();
		this.id_usuario = id_usuario;
		this.nombre_completo = nombre_completo;
		this.direccion = direccion;
		this.correo = correo;
		this.tipo_usuario = tipo_usuario;
	}

	public String getId_usuario() {
		return id_usuario;
	}

	public void setId_usuario(String id_usuario) {
		this.id_usuario = id_usuario;
	}

	public String getNombre_completo() {
		return nombre_completo;
	}

	public void setNombre_completo(String nombre_completo) {
		this.nombre_completo = nombre_completo;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTipo_usuario() {
		return tipo_usuario;
	}

	public void setTipo_usuario(String tipo_usuario) {
		this.tipo_usuario = tipo_usuario;
	}
	public boolean contieneCamposVacios() {
		return nombre_completo.isEmpty() || direccion.isEmpty() || correo.isEmpty() || tipo_usuario.isEmpty();
	}

}
