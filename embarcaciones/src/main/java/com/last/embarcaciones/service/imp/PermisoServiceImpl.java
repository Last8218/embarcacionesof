package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.entity.Permiso;
import com.last.embarcaciones.repository.PermisoRepository;
import com.last.embarcaciones.service.PermisoService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermisoServiceImpl implements PermisoService {

    @Autowired
    private PermisoRepository permisoRepository;

    @Override
    public List<Permiso> listarTodos() {
        return permisoRepository.findAll();
    }

    @Override
    public Optional<Permiso> buscarPorId(Integer idPermiso) {
        return permisoRepository.findById(idPermiso);
    }

    @Override
    public Optional<Permiso> buscarPorNombre(String nombre) {
        return permisoRepository.findByNombre(nombre);
    }

    @Override
    public Permiso guardar(Permiso permiso) {
        return permisoRepository.save(permiso);
    }

    @Override
    public void eliminar(Integer idPermiso) {
        permisoRepository.deleteById(idPermiso);
    }

    @Override
    @Transactional
    public void cambiarEstado(Integer idPermiso) {

        Permiso permiso = permisoRepository.findById(idPermiso)
                .orElseThrow(() ->
                        new RuntimeException("Permiso no encontrado"));

        permiso.setEstado(!permiso.getEstado());

        permisoRepository.save(permiso);
    }
}
