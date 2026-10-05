package com.last.embarcaciones.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Collections;
import java.util.Set;

@ControllerAdvice
public class ViewSessionAdvice {

    private static final String SESSION_PERMISOS = "permisosSesion";

    @ModelAttribute("permisosSesion")
    public Set<String> permisosSesion(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return Collections.emptySet();
        }

        Object permisos = session.getAttribute(SESSION_PERMISOS);
        if (permisos instanceof Set<?>) {
            @SuppressWarnings("unchecked")
            Set<String> cast = (Set<String>) permisos;
            return cast;
        }

        return Collections.emptySet();
    }
}

