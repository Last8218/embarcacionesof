package com.last.embarcaciones.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "embarcacion")
public class Embarcacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id_embarcacion;

    @Column(nullable = false, name = "codigo")
    private String codigo;

    @Column(nullable = false, name = "nombre")
    private String nombre;

    @Column(nullable = false, name = "tipo")
    private String tipo;

    @Column(nullable = false, name = "matricula")
    private String matricula;

    @Column(nullable = false, name = "estado")
    private String estado;

    @Column(nullable = false, name = "ubicacion")
    private String ubicacion;

    @Column(nullable = false, name = "descripcion")
    private String descripcion;

    @Column(nullable = true, name = "observaciones")
    private String observaciones;

    public Embarcacion() {
    }

    public  Embarcacion(int id_embarcacion) {
        this.id_embarcacion = id_embarcacion;
    }
    public Embarcacion ( String codigo) {
        this.codigo = codigo;
    }

    public Embarcacion(int id_embarcacion, String codigo,  String nombre, String tipo, String matricula, String estado, String ubicacion, String descripcion, String observaciones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.matricula = matricula;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
        this.codigo = codigo;
        this.id_embarcacion = id_embarcacion;
    }

    public Embarcacion(String codigo, String nombre, String tipo, String matricula, String estado, String ubicacion, String descripcion, String observaciones) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.tipo = tipo;
        this.matricula = matricula;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.observaciones = observaciones;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getId_embarcacion() {
        return id_embarcacion;
    }

    public void setId_embarcacion(int id_embarcacion) {
        this.id_embarcacion = id_embarcacion;
    }
}
