package com.gestix.service;

import com.gestix.config.GestixProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final GestixProperties gestixProperties;
    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        if (!gestixProperties.getMail().isEnabled()) {
            log.info("Recuperacion de contrasena para {} — enlace (solo dev): {}", to, resetLink);
            return;
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(gestixProperties.getMail().getFrom());
        message.setTo(to);
        message.setSubject("Restablece tu contrasena — Gestix");
        message.setText("""
                Hola,

                Recibimos una solicitud para restablecer tu contrasena en Gestix.

                Abre este enlace (valido 1 hora):
                %s

                Si no solicitaste este cambio, ignora este correo.

                — Equipo Gestix
                """.formatted(resetLink));

        mailSender.send(message);
        log.info("Correo de recuperacion enviado a {}", to);
    }

    public void logPasswordResetFallback(String to, String resetLink, Exception ex) {
        log.warn("No se pudo enviar el correo a {} ({}). Enlace valido: {}",
                to, ex.getMessage(), resetLink);
    }
}
