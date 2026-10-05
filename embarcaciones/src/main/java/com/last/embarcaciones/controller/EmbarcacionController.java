package com.last.embarcaciones.controller;


import com.last.embarcaciones.dto.EmbarcacionDTO;
import com.last.embarcaciones.service.EmbarcacionService;
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

import java.util.Set;

@Controller
@RequestMapping("/embarcaciones")
public class EmbarcacionController {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
            "OPERATIVO",
            "EN MANTENIMIENTO",
            "FUERA DE SERVICIO"
    );

    private final EmbarcacionService embarcacionService;

    public EmbarcacionController(EmbarcacionService embarcacionService) {
        this.embarcacionService = embarcacionService;
    }

    @GetMapping
    public String listar(HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("embarcaciones", embarcacionService.obtenerTodasLasEmbarcaciones());
        return "embarcaciones/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("embarcacion", new EmbarcacionDTO());
        model.addAttribute("modoEdicion", false);
        model.addAttribute("estadosValidos", ESTADOS_VALIDOS);
        return "embarcaciones/form";
    }

    @PostMapping
    public String crear(@ModelAttribute("embarcacion") EmbarcacionDTO embarcacionDTO,
                        HttpServletRequest request,
                        Model model,
                        RedirectAttributes redirectAttributes) {
        try {
            embarcacionDTO.setEstado(normalizarYValidarEstado(embarcacionDTO.getEstado()));
            embarcacionService.crearEmbarcacion(embarcacionDTO);
            redirectAttributes.addFlashAttribute("success", "Embarcacion creada exitosamente.");
            return "redirect:/embarcaciones";
        } catch (Exception e) {
            cargarLayout(model, request);
            model.addAttribute("modoEdicion", false);
            model.addAttribute("estadosValidos", ESTADOS_VALIDOS);
            model.addAttribute("error", e.getMessage());
            return "embarcaciones/form";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable int id, HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("embarcacion", embarcacionService.obtenerEmbarcacionPorId(id));
        return "embarcaciones/detalle";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable int id, HttpServletRequest request, Model model) {
        cargarLayout(model, request);
        model.addAttribute("embarcacion", embarcacionService.obtenerEmbarcacionPorId(id));
        model.addAttribute("modoEdicion", true);
        model.addAttribute("estadosValidos", ESTADOS_VALIDOS);
        return "embarcaciones/form";
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable int id,
                             @ModelAttribute("embarcacion") EmbarcacionDTO embarcacionDTO,
                             HttpServletRequest request,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        try {
            embarcacionDTO.setEstado(normalizarYValidarEstado(embarcacionDTO.getEstado()));
            embarcacionService.actualizarEmbarcacion(id, embarcacionDTO);
            redirectAttributes.addFlashAttribute("success", "Embarcacion actualizada exitosamente.");
            return "redirect:/embarcaciones";
        } catch (Exception e) {
            cargarLayout(model, request);
            model.addAttribute("modoEdicion", true);
            model.addAttribute("estadosValidos", ESTADOS_VALIDOS);
            model.addAttribute("error", e.getMessage());
            return "embarcaciones/form";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            embarcacionService.eliminarEmbarcacion(id);
            redirectAttributes.addFlashAttribute("success", "Embarcacion eliminada exitosamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/embarcaciones";
    }

    private void cargarLayout(Model model, HttpServletRequest request) {
        model.addAttribute("activeMenu", "embarcaciones");
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

    private String normalizarYValidarEstado(String estado) {
        if (estado == null) {
            throw new IllegalArgumentException("El estado es obligatorio");
        }
        String estadoNormalizado = estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(estadoNormalizado)) {
            throw new IllegalArgumentException("Estado invalido. Solo se permite: OPERATIVO, EN MANTENIMIENTO, FUERA DE SERVICIO");
        }
        return estadoNormalizado;
    }


}
