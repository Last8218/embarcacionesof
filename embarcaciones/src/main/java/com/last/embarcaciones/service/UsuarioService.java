package com.last.embarcaciones.service;

import com.last.embarcaciones.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {

    UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO);

    UsuarioDTO obtenerUsuarioPorId(int idUsuario);

    List<UsuarioDTO> obtenerTodosLosUsuarios();

    UsuarioDTO actualizarUsuario(int idUsuario, UsuarioDTO usuarioDTO);

    UsuarioDTO obtenerUsuarioPorUsuarioYContraseña(String usuario, String passwordHash);

    void eliminarUsuario(int idUsuario);



}
