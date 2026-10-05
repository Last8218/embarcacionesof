package com.last.embarcaciones.service;

import com.last.embarcaciones.entity.Rol;

import java.util.List;
import java.util.Optional;

public interface RolService {

    List<Rol> listarTodos();

    Optional<Rol> buscarPorId(Integer idRol);

    Optional<Rol> buscarPorNombre(String nombre);

    Rol guardar(Rol rol);

    void eliminar(Integer idRol);

    void cambiarEstado(Integer idRol);

    void asignarPermiso(Integer idRol, Integer idPermiso);

    void quitarPermiso(Integer idRol, Integer idPermiso);
}
