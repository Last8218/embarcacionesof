package com.last.embarcaciones.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private int idUsuario;

    @Column(nullable = false, unique = true, name = "usuario")

    private String usuario;

    @Column(nullable = false, name = "nombre")
    private String nombre;

    @Column(nullable = false, name = "apellido")
    private String apellido;

    @Column(nullable = false, unique = true, name = "email")
    private String email;

    @Column(nullable = false, name = "password_hash")
    private String passwordHash;

    @Column(name = "reset_token_hash", length = 64)
    private String resetTokenHash;

    @Column(name = "reset_token_expira_at")
    private LocalDateTime resetTokenExpiraAt;

    @Column(nullable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "estado")
    private int estado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_rol",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_rol")
    )
    private Set<Rol> roles = new HashSet<>();

    public Usuario(int idUsuario, String usuario, String nombre, String apellido, String email, String passwordHash, LocalDateTime createdAt, int estado) {
        this.idUsuario = idUsuario;
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.estado = estado;

    }

    public Usuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(String usuario, String nombre, String apellido, String email, String passwordHash) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Usuario(String usuario, String passwordHash) {
        this.usuario = usuario;
        this.passwordHash = passwordHash;
    }

    public Usuario() {
        // Constructor vacío requerido por JPA
    }

    public String getUsuario() {
        return usuario;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getEstado() {
        return estado;
    }

    public String getResetTokenHash() {
        return resetTokenHash;
    }

    public LocalDateTime getResetTokenExpiraAt() {
        return resetTokenExpiraAt;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setResetTokenHash(String resetTokenHash) {
        this.resetTokenHash = resetTokenHash;
    }

    public void setResetTokenExpiraAt(LocalDateTime resetTokenExpiraAt) {
        this.resetTokenExpiraAt = resetTokenExpiraAt;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public void agregarRol(Rol rol) {
        roles.add(rol);
    }

    public void eliminarRol(Rol rol) {
        roles.remove(rol);
    }
}
