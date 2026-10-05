package com.last.embarcaciones.service.imp;

import com.last.embarcaciones.exceptions.CorreoEnvioException;
import com.last.embarcaciones.service.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String from;
    private final String passwordResetConfirmUrl;

    public EmailServiceImpl(
            JavaMailSender mailSender,
            @Value("${app.mail.from}") String from,
            @Value("${app.password-reset.confirm-url}") String passwordResetConfirmUrl) {
        this.mailSender = mailSender;
        this.from = from;
        this.passwordResetConfirmUrl = passwordResetConfirmUrl;
    }

    @Override
    public void enviarCorreoRecuperacionPassword(String destinatario, String nombreUsuario, String token) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(destinatario);
            message.setSubject("Recuperacion de contrasena - Embarcaciones");
            message.setText(construirContenido(nombreUsuario, token));
            mailSender.send(message);
        } catch (Exception ex) {
            throw new CorreoEnvioException("No se pudo enviar el correo de recuperacion en este momento", ex);
        }
    }

    private String construirContenido(String nombreUsuario, String token) {
        String enlaceReset = construirEnlaceReset(token);
        return "Hola " + nombreUsuario + ",\n\n"
                + "Recibimos una solicitud para restablecer tu contrasena.\n\n"
                + "Abre este enlace para crear una nueva contrasena:\n"
                + enlaceReset + "\n\n"
                + "Si prefieres ingresar el token manualmente, usa este valor:\n"
                + token + "\n\n"
                + "Este token vence en 15 minutos.\n"
                + "Si no solicitaste este cambio, ignora este mensaje.";
    }

    private String construirEnlaceReset(String token) {
        String separador = passwordResetConfirmUrl.contains("?") ? "&" : "?";
        return passwordResetConfirmUrl + separador + "token=" + token;
    }
}

