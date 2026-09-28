package com.last.embarcaciones.controller;

import com.last.embarcaciones.dto.UsuarioDTO;
import com.last.embarcaciones.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
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
    @PostMapping("/api/login")
    public String login(@RequestParam String usuario, @RequestParam String passwordHash, Model model) {
        try {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuarioPorUsuarioYContraseña(usuario, passwordHash);
            return "redirect:/index"; // Redirige al index si las credenciales son correctas
        } catch (Exception e) {
            model.addAttribute("error", "Usuario no registrado o credenciales incorrectas.");
            return "login"; // Devuelve al login con el mensaje de error
        }
    }


    //Método para iniciar sesión
    @GetMapping("/login") // redirige al archivo login
    public String mostrarLogin() {
        return "login"; // Devuelve la vista login.html
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        String remoteUser = request.getRemoteUser(); // Obtiene el usuario logueado
        model.addAttribute("remoteUser", remoteUser);
        return "index";
    }

}
