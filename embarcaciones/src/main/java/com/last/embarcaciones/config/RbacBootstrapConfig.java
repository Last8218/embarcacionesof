package com.last.embarcaciones.config;

import com.last.embarcaciones.entity.Permiso;
import com.last.embarcaciones.entity.Rol;
import com.last.embarcaciones.repository.PermisoRepository;
import com.last.embarcaciones.repository.RolRepository;
import com.last.embarcaciones.utils.RbacPermissions;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Configuration
public class RbacBootstrapConfig {

    @Bean
    public CommandLineRunner seedRbacData(PermisoRepository permisoRepository, RolRepository rolRepository) {
        return args -> {
            List<String> permisosBase = Arrays.asList(
                    RbacPermissions.EMBARCACION_VER,
                    RbacPermissions.EMBARCACION_CREAR,
                    RbacPermissions.EMBARCACION_EDITAR,
                    RbacPermissions.EMBARCACION_ELIMINAR,
                    RbacPermissions.USUARIO_VER,
                    RbacPermissions.USUARIO_CREAR,
                    RbacPermissions.USUARIO_EDITAR,
                    RbacPermissions.USUARIO_ELIMINAR
            );

            Set<Permiso> permisosPersistidos = new LinkedHashSet<>();
            for (String nombrePermiso : permisosBase) {
                Permiso permiso = permisoRepository.findByNombre(nombrePermiso)
                        .orElseGet(() -> {
                            Permiso nuevo = new Permiso();
                            nuevo.setNombre(nombrePermiso);
                            nuevo.setDescripcion("Permiso " + nombrePermiso.toLowerCase(Locale.ROOT));
                            nuevo.setEstado(true);
                            return permisoRepository.save(nuevo);
                        });
                permisosPersistidos.add(permiso);
            }

            Rol admin = rolRepository.findByNombre("ADMIN")
                    .orElseGet(() -> {
                        Rol nuevoRol = new Rol();
                        nuevoRol.setNombre("ADMIN");
                        nuevoRol.setDescripcion("Rol administrador con acceso total");
                        nuevoRol.setEstado(true);
                        return rolRepository.save(nuevoRol);
                    });

            admin.setPermisos(new LinkedHashSet<>(permisosPersistidos));
            rolRepository.save(admin);
        };
    }
}

