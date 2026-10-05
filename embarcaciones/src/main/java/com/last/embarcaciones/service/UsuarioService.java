package com.last.embarcaciones.service;

import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.entity.Rol;

import java.util.List;
import java.util.Set;

public interface UsuarioService {

    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO obtenerUsuarioPorId(int idUsuario);

    List<UsuarioDTO> obtenerTodosLosUsuarios();

    UsuarioDTO actualizarUsuario(int idUsuario, UsuarioDTO usuarioDTO);

    UsuarioDTO obtenerUsuarioPorUsuarioYContraseña(String usuario, String password);

    void cambiarPassword(String usuario, String passwordActual, String passwordNueva);

    void solicitarRecuperacionPassword(String email);

    void confirmarRecuperacionPassword(String token, String passwordNueva);

    void eliminarUsuario(int idUsuario);

    void asignarRol(int idUsuario, int idRol);

    void quitarRol(int idUsuario, int idRol);

    List<Rol> obtenerRolesPorUsuario(int idUsuario);

    Set<String> obtenerPermisosPorUsuario(int idUsuario);



}
