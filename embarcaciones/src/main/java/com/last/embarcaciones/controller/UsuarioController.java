package com.last.embarcaciones.controller;


import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.service.UsuarioService;
import com.last.embarcaciones.utils.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public GenericResponse<UsuarioDTO> crearUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);
        return new GenericResponse<>("success", 1, "Usuario creado exitosamente", usuarioCreado);
    }

    @GetMapping("/{id}")
    public GenericResponse<UsuarioDTO> obtenerUsuarioPorId(@PathVariable int id) {
        UsuarioDTO usuario = usuarioService.obtenerUsuarioPorId(id);
        return new GenericResponse<>("success", 1, "Usuario obtenido exitosamente", usuario);
    }

    @GetMapping
    public GenericResponse<List<UsuarioDTO>> listarUsuarios() {
        List<UsuarioDTO> usuarios = usuarioService.obtenerTodosLosUsuarios();
        return new GenericResponse<>("success", 1, "Usuarios obtenidos exitosamente", usuarios);
    }

    @GetMapping("/login")
    public GenericResponse<UsuarioDTO> login(@RequestParam String usuario, @RequestParam String passwordHash) {
        UsuarioDTO usuarioEncontrado = usuarioService.obtenerUsuarioPorUsuarioYContraseña(usuario, passwordHash);
        return new GenericResponse<>("success", 1, "Usuario encontrado exitosamente", usuarioEncontrado);
    }

    @PutMapping("/{id}")
    public GenericResponse<UsuarioDTO> actualizarUsuario(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return new GenericResponse<>("success", 1, "Usuario actualizado exitosamente", usuarioActualizado);
    }


}
