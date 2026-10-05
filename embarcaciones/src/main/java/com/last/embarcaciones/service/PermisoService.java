package com.last.embarcaciones.service;

import com.last.embarcaciones.entity.Permiso;

import java.util.List;
import java.util.Optional;

public interface PermisoService {

    List<Permiso> listarTodos();

    Optional<Permiso> buscarPorId(Integer idPermiso);

    Optional<Permiso> buscarPorNombre(String nombre);

    Permiso guardar(Permiso permiso);

    void eliminar(Integer idPermiso);

    void cambiarEstado(Integer idPermiso);
}
