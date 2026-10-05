package com.last.embarcaciones.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "rol")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private Integer idRol;


    @Column(nullable = false, name = "nombre", unique = true, length = 100)
    private String nombre;

    @Column(nullable = true, name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;


    @ManyToMany
    @JoinTable(
            name = "rol_permiso",
            joinColumns = @JoinColumn(name = "id_rol"),
            inverseJoinColumns = @JoinColumn(name = "id_permiso")
    )
    private Set<Permiso> permisos = new HashSet<>();


    public Rol() {

    }

    public Rol(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public Rol(String nombre, Set<Permiso> permisos, String descripcion) {
        this.nombre = nombre;
        this.permisos = permisos;
        this.descripcion = descripcion;
    }

    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getIdRol() {
        return idRol;
    }

    public void setIdRol(Integer idRol) {
        this.idRol = idRol;
    }
}
