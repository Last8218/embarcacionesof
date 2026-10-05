package com.last.embarcaciones.controller;

import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService usuarioService;

    public UsuarioViewController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listarUsuarios(HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("usuarios", usuarioService.obtenerTodosLosUsuarios());
        return "usuarios/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("usuario", new UsuarioDTO());
        model.addAttribute("modoEdicion", false);
        return "usuarios/form";
    }

    @PostMapping
    public String crearUsuario(@ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                               HttpServletRequest request,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            usuarioService.crearUsuario(usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario creado exitosamente.");
            return "redirect:/usuarios";
        } catch (Exception e) {
            cargarLayout(model, request);
            model.addAttribute("modoEdicion", false);
            model.addAttribute("error", e.getMessage());
            return "usuarios/form";
        }
    }

    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable int id, HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("usuario", usuarioService.obtenerUsuarioPorId(id));
        model.addAttribute("modoEdicion", true);
        return "usuarios/form";
    }

    @PostMapping("/{id}")
    public String actualizarUsuario(@PathVariable int id,
                                    @ModelAttribute("usuario") UsuarioDTO usuarioDTO,
                                    HttpServletRequest request,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        try {
            usuarioService.actualizarUsuario(id, usuarioDTO);
            redirectAttributes.addFlashAttribute("success", "Usuario actualizado exitosamente.");
            return "redirect:/usuarios";
        } catch (Exception e) {
            cargarLayout(model, request);
            model.addAttribute("modoEdicion", true);
            model.addAttribute("error", e.getMessage());
            return "usuarios/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarUsuario(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            usuarioService.eliminarUsuario(id);
            redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    private void cargarLayout(Model model, HttpServletRequest request) {
        model.addAttribute("activeMenu", "usuarios");
        model.addAttribute("remoteUser", obtenerUsuarioVisual(request));
    }

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
}

