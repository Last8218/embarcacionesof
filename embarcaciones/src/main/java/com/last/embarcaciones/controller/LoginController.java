package com.last.embarcaciones.controller;

import com.last.embarcaciones.service.EmbarcacionService;
import com.last.embarcaciones.service.UsuarioService;
import com.last.embarcaciones.dto.UsuarioDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EmbarcacionService embarcacionService;

    @PostMapping("/api/login")
    public String login(@RequestParam String usuario, @RequestParam String password, Model model, HttpServletRequest request) {
        try {
            UsuarioDTO usuarioAutenticado = usuarioService.obtenerUsuarioPorUsuarioYContraseña(usuario, password);
            HttpSession session = request.getSession(true);
            session.setAttribute("usuarioLogueado", usuarioAutenticado.getUsuario());
            session.setAttribute("idUsuarioLogueado", usuarioAutenticado.getIdUsuario());
            session.setAttribute("permisosSesion", usuarioService.obtenerPermisosPorUsuario(usuarioAutenticado.getIdUsuario()));
            return "redirect:/index"; // Redirige al index si las credenciales son correctas
        } catch (Exception e) {
            model.addAttribute("error", "Usuario no registrado o credenciales incorrectas.");
            return "login"; // Devuelve al login con el mensaje de error
        }
    }


    //Método para iniciar sesión
    @GetMapping("/login") // redirige al archivo login
    public String mostrarLogin(@RequestParam(required = false) Boolean denied, Model model) {
        if (Boolean.TRUE.equals(denied)) {
            model.addAttribute("error", "No tienes permisos para acceder a esta seccion.");
        }
        return "login"; // Devuelve la vista login.html
    }

    //Método para mostrar la página de solicitud de recuperación de contraseña
    @GetMapping("/password/forgot")
    public String mostrarSolicitudRecuperacion() {
        return "password-recovery-request";
    }

    //Método para procesar la solicitud de recuperación de contraseña
    @PostMapping("/password/forgot")
    public String solicitarRecuperacionPassword(@RequestParam String email, Model model) {
        try {
            usuarioService.solicitarRecuperacionPassword(email);
            model.addAttribute("success", "Si el correo existe, se envio un enlace para recuperar la contrasena.");
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "password-recovery-request";
    }

    @GetMapping("/password/reset")
    public String mostrarResetPassword(@RequestParam(required = false) String token, Model model) {
        model.addAttribute("token", token);
        if (token == null || token.isBlank()) {
            model.addAttribute("error", "Token no valido. Solicita nuevamente la recuperacion.");
        }
        return "password-recovery-reset";
    }

    @PostMapping("/password/reset")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String passwordNueva,
            @RequestParam String passwordConfirmacion,
            Model model) {

        if (!passwordNueva.equals(passwordConfirmacion)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "Las contrasenas no coinciden.");
            return "password-recovery-reset";
        }

        try {
            usuarioService.confirmarRecuperacionPassword(token, passwordNueva);
            return "redirect:/login?resetSuccess=true";
        } catch (Exception e) {
            model.addAttribute("token", token);
            model.addAttribute("error", e.getMessage());
            return "password-recovery-reset";
        }
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request,
                        @RequestParam(required = false) Boolean denied,
                        Model model) {
        var embarcaciones = embarcacionService.obtenerTodasLasEmbarcaciones();
        long operativas = embarcaciones.stream().filter(e -> esOperativa(e.getEstado())).count();

        model.addAttribute("remoteUser", obtenerUsuarioVisual(request));
        model.addAttribute("activeMenu", "dashboard");
        model.addAttribute("totalEmbarcaciones", embarcaciones.size());
        model.addAttribute("embarcacionesOperativas", operativas);
        if (Boolean.TRUE.equals(denied)) {
            model.addAttribute("error", "No tienes permisos para acceder a la seccion solicitada.");
        }
        return "index";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logoutSuccess=true";
    }

    @GetMapping("/seccion/embarcaciones")
    public String embarcaciones(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "embarcaciones", "Embarcaciones");
    }

    @GetMapping("/seccion/maquinas-sistemas")
    public String maquinasSistemas(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "maquinas-sistemas", "Maquinas y Sistemas");
    }

    @GetMapping("/seccion/componentes")
    public String componentes(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "componentes", "Componentes");
    }

    @GetMapping("/seccion/piezas-repuestos")
    public String piezasRepuestos(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "piezas-repuestos", "Piezas y Repuestos");
    }

    @GetMapping("/seccion/documentacion")
    public String documentacion(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "documentacion", "Documentacion");
    }

    @GetMapping("/seccion/mantenimiento-historial")
    public String mantenimientoHistorial(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "mantenimiento-historial", "Mantenimiento e Historial");
    }

    @GetMapping("/seccion/informacion-tecnica")
    public String informacionTecnica(HttpServletRequest request, Model model) {
        return renderSeccion(request, model, "informacion-tecnica", "Informacion Tecnica");
    }

    // Método auxiliar para renderizar las secciones con los atributos comunes
    private String renderSeccion(HttpServletRequest request, Model model, String activeMenu, String sectionTitle) {
        model.addAttribute("remoteUser", obtenerUsuarioVisual(request));
        model.addAttribute("activeMenu", activeMenu);
        model.addAttribute("sectionTitle", sectionTitle);
        return "section-placeholder";
    }

    // Método para obtener el usuario visual desde la sesión o el usuario remoto
    private String obtenerUsuarioVisual(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object usuarioSesion = session.getAttribute("usuarioLogueado");
            if (usuarioSesion != null) {
                return usuarioSesion.toString();
            }
        }
        return request.getRemoteUser();
    }

    //Extraer la cantidad de embarcaciones operativas del total de embarcaciones
    private boolean esOperativa(String estado) {
        if (estado == null) {
            return false;
        }
        String estadoNormalizado = estado.trim().toUpperCase();
        return "OPERATIVO".equals(estadoNormalizado) || "OPERATIVA".equals(estadoNormalizado);
    }

}
