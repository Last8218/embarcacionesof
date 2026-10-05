package com.last.embarcaciones.service;

public interface EmailService {

    void enviarCorreoRecuperacionPassword(String destinatario, String nombreUsuario, String token);
}

