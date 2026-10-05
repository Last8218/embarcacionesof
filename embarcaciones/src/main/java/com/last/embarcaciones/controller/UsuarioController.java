package com.last.embarcaciones.controller;


import com.last.embarcaciones.dto.CambioPasswordRequest;
import com.last.embarcaciones.dto.ConfirmacionRecuperacionPasswordRequest;
import com.last.embarcaciones.dto.SolicitudRecuperacionPasswordRequest;
import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.entity.Rol;
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
    public GenericResponse<UsuarioDTO> login(@RequestParam String usuario, @RequestParam String password) {
        UsuarioDTO usuarioEncontrado = usuarioService.obtenerUsuarioPorUsuarioYContraseña(usuario, password);
        return new GenericResponse<>("success", 1, "Usuario encontrado exitosamente", usuarioEncontrado);
    }

    @PutMapping("/{id}")
    public GenericResponse<UsuarioDTO> actualizarUsuario(@PathVariable int id, @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(id, usuarioDTO);
        return new GenericResponse<>("success", 1, "Usuario actualizado exitosamente", usuarioActualizado);
    }

    @PostMapping("/password/cambiar")
    public GenericResponse<Object> cambiarPassword(@RequestBody CambioPasswordRequest request) {
        usuarioService.cambiarPassword(request.getUsuario(), request.getPasswordActual(), request.getPasswordNueva());
        return new GenericResponse<>("success", 1, "Contrasena actualizada exitosamente", null);
    }

    @PostMapping("/password/recuperar/solicitud")
    public GenericResponse<Object> solicitarRecuperacionPassword(@RequestBody SolicitudRecuperacionPasswordRequest request) {
        usuarioService.solicitarRecuperacionPassword(request.getEmail());
        return new GenericResponse<>("success", 1, "Si el correo existe, se envio un mensaje de recuperacion.", null);
    }

    @PostMapping("/password/recuperar/confirmar")
    public GenericResponse<Object> confirmarRecuperacionPassword(@RequestBody ConfirmacionRecuperacionPasswordRequest request) {
        usuarioService.confirmarRecuperacionPassword(request.getToken(), request.getPasswordNueva());
        return new GenericResponse<>("success", 1, "Contrasena restablecida exitosamente", null);
    }

    @PostMapping("/{idUsuario}/roles/{idRol}")
    public GenericResponse<Object> asignarRol(@PathVariable int idUsuario, @PathVariable int idRol) {
        usuarioService.asignarRol(idUsuario, idRol);
        return new GenericResponse<>("success", 1, "Rol asignado exitosamente", null);
    }

    @DeleteMapping("/{idUsuario}/roles/{idRol}")
    public GenericResponse<Object> quitarRol(@PathVariable int idUsuario, @PathVariable int idRol) {
        usuarioService.quitarRol(idUsuario, idRol);
        return new GenericResponse<>("success", 1, "Rol retirado exitosamente", null);
    }

    @GetMapping("/{idUsuario}/roles")
    public GenericResponse<List<Rol>> obtenerRolesPorUsuario(@PathVariable int idUsuario) {
        List<Rol> roles = usuarioService.obtenerRolesPorUsuario(idUsuario);
        return new GenericResponse<>("success", 1, "Roles obtenidos exitosamente", roles);
    }


}
