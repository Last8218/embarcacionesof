package com.last.embarcaciones.config;

import com.last.embarcaciones.utils.RbacPermissions;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Collections;
import java.util.Set;

@Component
public class RbacInterceptor implements HandlerInterceptor {

    private static final String SESSION_USUARIO_ID = "idUsuarioLogueado";
    private static final String SESSION_PERMISOS = "permisosSesion";

    // El método preHandle se ejecuta antes de que el controlador maneje la solicitud HTTP.
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = getPath(request);

        // Verifica si la ruta es pública (no requiere autenticación)
        if (esRutaPublica(path)) {
            return true;
        }

        // Verifica si el usuario está autenticado
        HttpSession session = request.getSession(false);

        // Si no hay sesión o no hay un usuario autenticado, redirige al login
        if (session == null || session.getAttribute(SESSION_USUARIO_ID) == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        // Verifica si el usuario tiene los permisos necesarios para acceder a la ruta
        String permisoRequerido = resolverPermiso(request.getMethod(), path);

        // Si no se requiere un permiso específico para la ruta, permite el acceso
        if (permisoRequerido == null) {
            return true;
        }

        // Obtiene los permisos del usuario desde la sesión y verifica si tiene el permiso requerido
        Set<String> permisosSesion = obtenerPermisos(session);

        // Si el usuario tiene el permiso requerido, permite el acceso
        if (permisosSesion.contains(permisoRequerido)) {
            return true;
        }

        // Si el usuario no tiene el permiso requerido, maneja la respuesta según el tipo de solicitud
        if (path.startsWith("/api/")) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "No tienes permisos para esta operacion");
            return false;
        }

        // Si es una solicitud normal, redirige al index con un mensaje de acceso denegado
        response.sendRedirect(request.getContextPath() + "/index?denied=true");
        return false;
    }

    // Método auxiliar para obtener la ruta de la solicitud, eliminando el contexto de la aplicación
    private String getPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();
        if (contextPath != null && !contextPath.isBlank() && uri.startsWith(contextPath)) {
            return uri.substring(contextPath.length());
        }
        return uri;
    }

    // Método auxiliar para determinar si una ruta es pública (no requiere autenticación)
    private boolean esRutaPublica(String path) {
        return path.equals("/login")
                || path.equals("/api/login")
                || path.startsWith("/api/")
                || path.startsWith("/password/")
                || path.startsWith("/css/")
                || path.startsWith("/js/")
                || path.startsWith("/images/")
                || path.startsWith("/webjars/")
                || path.equals("/error");
    }

    // Método auxiliar para obtener los permisos del usuario desde la sesión
    private Set<String> obtenerPermisos(HttpSession session) {
        Object permisos = session.getAttribute(SESSION_PERMISOS);
        if (permisos instanceof Set<?>) {
            @SuppressWarnings("unchecked")
            Set<String> cast = (Set<String>) permisos;
            return cast;
        }
        return Collections.emptySet();
    }

    // Método auxiliar para resolver el permiso requerido según el método HTTP y la ruta
    private String resolverPermiso(String method, String path) {
        if ("GET".equalsIgnoreCase(method) && path.equals("/usuarios")) {
            return RbacPermissions.USUARIO_VER;
        }
        if ("GET".equalsIgnoreCase(method) && path.equals("/usuarios/nuevo")) {
            return RbacPermissions.USUARIO_CREAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.equals("/usuarios")) {
            return RbacPermissions.USUARIO_CREAR;
        }
        if ("GET".equalsIgnoreCase(method) && path.matches("^/usuarios/\\d+/editar$")) {
            return RbacPermissions.USUARIO_EDITAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.matches("^/usuarios/\\d+$")) {
            return RbacPermissions.USUARIO_EDITAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.matches("^/usuarios/\\d+/eliminar$")) {
            return RbacPermissions.USUARIO_ELIMINAR;
        }

        if ("GET".equalsIgnoreCase(method) && path.equals("/embarcaciones")) {
            return RbacPermissions.EMBARCACION_VER;
        }
        if ("GET".equalsIgnoreCase(method) && path.matches("^/embarcaciones/\\d+$")) {
            return RbacPermissions.EMBARCACION_VER;
        }
        if ("GET".equalsIgnoreCase(method) && path.equals("/embarcaciones/nuevo")) {
            return RbacPermissions.EMBARCACION_CREAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.equals("/embarcaciones")) {
            return RbacPermissions.EMBARCACION_CREAR;
        }
        if ("GET".equalsIgnoreCase(method) && path.matches("^/embarcaciones/\\d+/editar$")) {
            return RbacPermissions.EMBARCACION_EDITAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.matches("^/embarcaciones/\\d+$")) {
            return RbacPermissions.EMBARCACION_EDITAR;
        }
        if ("POST".equalsIgnoreCase(method) && path.matches("^/embarcaciones/\\d+/eliminar$")) {
            return RbacPermissions.EMBARCACION_ELIMINAR;
        }

        return null;
    }
}

