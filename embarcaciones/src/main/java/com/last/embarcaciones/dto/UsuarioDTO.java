package com.last.embarcaciones.dto;

import lombok.Data;


public class UsuarioDTO {

    private Integer idUsuario;
    private String usuario;
    private String nombre;
    private String apellido;
    private String email;
    private String passwordHash;
    private int estado;

    public UsuarioDTO() {
        // Constructor privado para evitar la creación directa de instancias
    }

    public UsuarioDTO (String usuario, String PasswordHash) {
        this.usuario = usuario;
        this.passwordHash = passwordHash;
    }

    public UsuarioDTO(Integer idUsuario, String usuario, String nombre, String apellido, String email, String passwordHash, int estado) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.passwordHash = passwordHash;
        this.estado = estado;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}
