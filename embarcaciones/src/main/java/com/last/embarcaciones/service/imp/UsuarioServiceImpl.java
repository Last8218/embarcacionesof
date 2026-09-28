package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.entity.Usuario;
import com.last.embarcaciones.exceptions.EntityNotFoundException;
import com.last.embarcaciones.exceptions.UsuarioNoEncontradoException;
import com.last.embarcaciones.repository.UsuarioRepository;
import com.last.embarcaciones.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;


    @Override
    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = mapearEntidad(usuarioDTO);
        Usuario usuarioNuevo = usuarioRepository.save(usuario);

        return mapearDTO(usuarioNuevo);
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(int idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(()-> new EntityNotFoundException("Usuario", "ID: " + idUsuario));
        return mapearDTO(usuario);
    }

    @Override
    public List<UsuarioDTO> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        return usuarios.stream().map(this::mapearDTO).collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO actualizarUsuario(int idUsuario, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setPasswordHash(usuarioDTO.getPasswordHash());
        usuario.setEstado(usuarioDTO.getEstado());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return mapearDTO(usuarioActualizado);
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorUsuarioYContraseña(String usuario, String passwordHash) {
        Usuario usuarioEncontrado = usuarioRepository.findByUsuarioAndPasswordHash(usuario, passwordHash)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no se encuentra registrado o las credenciales son incorrecta"));

        return mapearDTO(usuarioEncontrado);
    }

    @Override
    public void eliminarUsuario(int idUsuario) {

    }

    private Usuario mapearEntidad(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setPasswordHash(usuarioDTO.getPasswordHash());
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setEstado(usuarioDTO.getEstado());

        return usuario;
    }

    private UsuarioDTO mapearDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(usuario.getIdUsuario());
        usuarioDTO.setUsuario(usuario.getUsuario());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setEmail(usuario.getEmail());
        usuarioDTO.setEstado(usuario.getEstado());
        usuarioDTO.setPasswordHash(usuario.getPasswordHash());
        return usuarioDTO;
    }

}
