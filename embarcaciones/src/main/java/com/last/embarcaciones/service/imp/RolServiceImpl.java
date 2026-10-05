package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.entity.Permiso;
import com.last.embarcaciones.entity.Rol;
import com.last.embarcaciones.repository.PermisoRepository;
import com.last.embarcaciones.repository.RolRepository;
import com.last.embarcaciones.service.RolService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private  RolRepository rolRepository;

    @Autowired
    private  PermisoRepository permisoRepository;


    @Override
    public List<Rol> listarTodos() {
        return rolRepository.findAll();
    }

    @Override
    public Optional<Rol> buscarPorId(Integer idRol) {
        return rolRepository.findById(idRol);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String nombre) {
        return rolRepository.findByNombre(nombre);
    }

    @Override
    public Rol guardar(Rol rol) {
        return rolRepository.save(rol);
    }

    @Override
    public void eliminar(Integer idRol) {
        rolRepository.deleteById(idRol);
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer idRol) {

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        rol.setEstado(!rol.getEstado());

        rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void asignarPermiso(Integer idRol, Integer idPermiso) {

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() ->
                        new RuntimeException("Permiso no encontrado"));

        rol.getPermisos().add(permiso);

        rolRepository.save(rol);
    }

    @Override
    @Transactional
    public void quitarPermiso(Integer idRol, Integer idPermiso) {

        Rol rol = rolRepository.findById(idRol)
                .orElseThrow(() ->
                        new RuntimeException("Rol no encontrado"));

        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() ->
                        new RuntimeException("Permiso no encontrado"));

        rol.getPermisos().remove(permiso);

        rolRepository.save(rol);
    }
}
