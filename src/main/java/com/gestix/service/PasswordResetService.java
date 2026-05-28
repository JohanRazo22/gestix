package com.gestix.service;

import com.gestix.config.GestixProperties;
import com.gestix.dto.ForgotPasswordResponse;
import com.gestix.dto.MessageResponse;
import com.gestix.entity.PasswordResetToken;
import com.gestix.entity.User;
import com.gestix.repository.PasswordResetTokenRepository;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private static final String GENERIC_FORGOT_MESSAGE =
            "Si el correo esta registrado, recibiras un enlace para restablecer tu contrasena.";

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final GestixProperties gestixProperties;
    private final TransactionTemplate transactionTemplate;

    public ForgotPasswordResponse requestReset(String email) {
        var userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return ForgotPasswordResponse.builder().message(GENERIC_FORGOT_MESSAGE).build();
        }

        User user = userOpt.get();
        String token = transactionTemplate.execute(status -> {
            tokenRepository.deleteByUser(user);
            String newToken = UUID.randomUUID().toString();
            tokenRepository.save(PasswordResetToken.builder()
                    .token(newToken)
                    .user(user)
                    .expiresAt(LocalDateTime.now().plusHours(1))
                    .build());
            return newToken;
        });

        String resetLink = gestixProperties.getAppUrl() + "/reset-password.html?token=" + token;
        boolean emailSent = false;

        try {
            emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
            emailSent = true;
        } catch (Exception ex) {
            emailService.logPasswordResetFallback(user.getEmail(), resetLink, ex);
        }

        ForgotPasswordResponse.ForgotPasswordResponseBuilder response = ForgotPasswordResponse.builder()
                .message(GENERIC_FORGOT_MESSAGE);

        if (!emailSent && isLocalDev()) {
            response.devHint("No se pudo enviar el correo (Gmail rechazó la contraseña SMTP). "
                    + "Usa este enlace para restablecer tu contraseña (válido 1 hora): "
                    + resetLink);
        }

        return response.build();
    }

    private boolean isLocalDev() {
        String url = gestixProperties.getAppUrl();
        return url != null && (url.contains("localhost") || url.contains("127.0.0.1"));
    }

    @Transactional
    public MessageResponse resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = tokenRepository.findByTokenAndUsedFalse(token)
                .orElseThrow(() -> new IllegalArgumentException("Enlace invalido o expirado"));

        if (resetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Enlace invalido o expirado");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetToken.setUsed(true);
        tokenRepository.save(resetToken);

        return MessageResponse.of("Contrasena actualizada correctamente. Ya puedes iniciar sesion.");
    }
}
