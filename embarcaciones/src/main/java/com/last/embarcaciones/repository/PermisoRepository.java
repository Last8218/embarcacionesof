package com.last.embarcaciones.repository;

import com.last.embarcaciones.entity.Permiso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermisoRepository  extends JpaRepository<Permiso, Integer> {

    Optional<Permiso> findByNombre(String nombre);


}
