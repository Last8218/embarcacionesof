package com.last.embarcaciones.dto;

public class EmbarcacionDTO {

    private int idEmbarcacion;
    private String codigo;
    private String nombre;
    private String tipo;
    private String matricula;
    private String estado;
    private String ubicacion;
    private String descripcion;
    private String obsevaciones;

    public EmbarcacionDTO() {
        // Constructor vacío
    }

    public EmbarcacionDTO(int idEmbarcacion) {
        this.idEmbarcacion = idEmbarcacion;
    }

    public EmbarcacionDTO(String codigo) {
        this.codigo = codigo;
    }

    public EmbarcacionDTO(int idEmbarcacion, String nombre, String tipo, String matricula, String estado, String ubicacion, String descripcion, String obsevaciones) {
        this.idEmbarcacion = idEmbarcacion;
        this.nombre = nombre;
        this.tipo = tipo;
        this.matricula = matricula;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.obsevaciones = obsevaciones;
    }

    public EmbarcacionDTO(String nombre, String tipo, String matricula, String estado, String ubicacion, String descripcion, String obsevaciones) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.matricula = matricula;
        this.estado = estado;
        this.ubicacion = ubicacion;
        this.descripcion = descripcion;
        this.obsevaciones = obsevaciones;
    }

    public int getIdEmbarcacion() {
        return idEmbarcacion;
    }

    public void setIdEmbarcacion(int idEmbarcacion) {
        this.idEmbarcacion = idEmbarcacion;
    }

    public String getObsevaciones() {
        return obsevaciones;
    }

    public void setObsevaciones(String obsevaciones) {
        this.obsevaciones = obsevaciones;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
