package com.last.embarcaciones.repository;

import com.last.embarcaciones.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsuarioAndPasswordHash(String usuario, String passwordHash);
}
