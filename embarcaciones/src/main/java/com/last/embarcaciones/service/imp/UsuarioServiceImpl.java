package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.entity.Rol;
import com.last.embarcaciones.entity.Usuario;
import com.last.embarcaciones.exceptions.EntityNotFoundException;
import com.last.embarcaciones.exceptions.UsuarioNoEncontradoException;
import com.last.embarcaciones.repository.RolRepository;
import com.last.embarcaciones.repository.UsuarioRepository;
import com.last.embarcaciones.service.EmailService;
import com.last.embarcaciones.service.UsuarioService;
import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HexFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private static final int RESET_TOKEN_EXPIRACION_MINUTOS = 15;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;


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
        // El cambio de password debe pasar por el endpoint dedicado.
        usuario.setEstado(usuarioDTO.getEstado());

        Usuario usuarioActualizado = usuarioRepository.save(usuario);
        return mapearDTO(usuarioActualizado);
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorUsuarioYContraseña(String usuario, String password) {
        Usuario usuarioEncontrado = usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no se encuentra registrado o las credenciales son incorrectas"));

        String passwordGuardada = usuarioEncontrado.getPasswordHash();
        boolean passwordValida = passwordEncoder.matches(password, passwordGuardada);

        // Migracion transparente de contraseñas legacy en texto plano.
        if (!passwordValida && !esHashBCrypt(passwordGuardada) && passwordGuardada.equals(password)) {
            usuarioEncontrado.setPasswordHash(passwordEncoder.encode(password));
            usuarioRepository.save(usuarioEncontrado);
            passwordValida = true;
        }

        if (!passwordValida) {
            throw new UsuarioNoEncontradoException("El usuario no se encuentra registrado o las credenciales son incorrectas");
        }

        return mapearDTO(usuarioEncontrado);
    }

    @Override
    public void cambiarPassword(String usuario, String passwordActual, String passwordNueva) {
        Usuario usuarioEncontrado = usuarioRepository.findByUsuario(usuario)
                .orElseThrow(() -> new UsuarioNoEncontradoException("El usuario no se encuentra registrado"));

        if (!passwordEncoder.matches(passwordActual, usuarioEncontrado.getPasswordHash())) {
            throw new IllegalArgumentException("La contrasena actual no es correcta");
        }

        validarPasswordNueva(passwordNueva);

        if (passwordEncoder.matches(passwordNueva, usuarioEncontrado.getPasswordHash())) {
            throw new IllegalArgumentException("La nueva contrasena no puede ser igual a la actual");
        }

        usuarioEncontrado.setPasswordHash(passwordEncoder.encode(passwordNueva));
        usuarioEncontrado.setResetTokenHash(null);
        usuarioEncontrado.setResetTokenExpiraAt(null);
        usuarioRepository.save(usuarioEncontrado);
    }

    @Override
    public void solicitarRecuperacionPassword(String email) {
        Usuario usuarioEncontrado = usuarioRepository.findByEmail(email)
                .orElse(null);

        if (usuarioEncontrado == null) {
            return;
        }

        String tokenPlano = generarTokenSeguro();
        usuarioEncontrado.setResetTokenHash(hashToken(tokenPlano));
        usuarioEncontrado.setResetTokenExpiraAt(LocalDateTime.now().plusMinutes(RESET_TOKEN_EXPIRACION_MINUTOS));
        usuarioRepository.save(usuarioEncontrado);
        emailService.enviarCorreoRecuperacionPassword(
                usuarioEncontrado.getEmail(),
                usuarioEncontrado.getNombre(),
                tokenPlano
        );
    }

    @Override
    public void confirmarRecuperacionPassword(String token, String passwordNueva) {
        String tokenHash = hashToken(token);
        Usuario usuarioEncontrado = usuarioRepository.findByResetTokenHash(tokenHash)
                .orElseThrow(() -> new IllegalArgumentException("Token invalido o expirado"));

        LocalDateTime expiracion = usuarioEncontrado.getResetTokenExpiraAt();
        if (expiracion == null || expiracion.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token invalido o expirado");
        }

        validarPasswordNueva(passwordNueva);

        usuarioEncontrado.setPasswordHash(passwordEncoder.encode(passwordNueva));
        usuarioEncontrado.setResetTokenHash(null);
        usuarioEncontrado.setResetTokenExpiraAt(null);
        usuarioRepository.save(usuarioEncontrado);
    }

    @Override
    public void eliminarUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntityNotFoundException("Usuario", "ID: " + idUsuario));
        usuario.setEstado(0);
        usuarioRepository.save(usuario);
    }

    private Usuario mapearEntidad(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();

        usuario.setUsuario(usuarioDTO.getUsuario());
        usuario.setPasswordHash(passwordEncoder.encode(usuarioDTO.getPasswordHash()));
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
        usuarioDTO.setPasswordHash(null);
        return usuarioDTO;
    }

    private boolean esHashBCrypt(String passwordHash) {
        return passwordHash != null && passwordHash.matches("^\\$2[aby]?\\$\\d{2}\\$.{53}$");
    }

    private void validarPasswordNueva(String passwordNueva) {
        if (passwordNueva == null || passwordNueva.length() < 8) {
            throw new IllegalArgumentException("La nueva contrasena debe tener al menos 8 caracteres");
        }
    }

    private String generarTokenSeguro() {
        byte[] randomBytes = new byte[32];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    private String hashToken(String tokenPlano) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(tokenPlano.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo generar el hash del token", e);
        }
    }

    @Override
    @Transactional
    public void asignarRol(int idUsuario, int idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario", "ID: " + idUsuario));

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new EntityNotFoundException("Rol", "ID: " + idRol));

        if (usuario.getRoles().contains(rol)) {
            throw new IllegalArgumentException(
                    "El usuario ya tiene asignado este rol"
            );
        }

        usuario.agregarRol(rol);

        usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public void quitarRol(int idUsuario, int idRol) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario", "ID: " + idUsuario));

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new EntityNotFoundException("Rol", "ID: " + idRol));

        usuario.eliminarRol(rol);

        usuarioRepository.save(usuario);
    }

    @Override

    public List<Rol> obtenerRolesPorUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario", "ID: " + idUsuario));

        return new ArrayList<>(usuario.getRoles());
    }

    @Override
    public Set<String> obtenerPermisosPorUsuario(int idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() ->
                        new EntityNotFoundException("Usuario", "ID: " + idUsuario));

        return usuario.getRoles().stream()
                .filter(rol -> rol.getEstado() == null || rol.getEstado())
                .flatMap(rol -> rol.getPermisos().stream())
                .filter(permiso -> permiso.getEstado() == null || permiso.getEstado())
                .map(permiso -> permiso.getNombre() == null ? "" : permiso.getNombre().trim().toUpperCase())
                .filter(nombre -> !nombre.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

}
