package com.gestix.service;

import com.gestix.config.GestixProperties;
import com.gestix.dto.ForgotPasswordResponse;
import com.gestix.dto.MessageResponse;
import com.gestix.entity.PasswordResetToken;
import com.gestix.entity.User;
import com.gestix.repository.PasswordResetTokenRepository;
import com.gestix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PasswordResetService - Pruebas unitarias")
class PasswordResetServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordResetTokenRepository tokenRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EmailService emailService;
    @Mock private GestixProperties gestixProperties;
    @Mock private TransactionTemplate transactionTemplate;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("OLD_HASH")
                .build();
    }

    private void stubAppUrl() {
        when(gestixProperties.getAppUrl()).thenReturn("http://localhost:8080");
    }

    @Test
    @DisplayName("requestReset: email existente - crea token y envia correo")
    void requestReset_existingEmail_createsToken() {
        stubAppUrl();
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(sampleUser));
        when(transactionTemplate.execute(org.mockito.ArgumentMatchers.<org.springframework.transaction.support.TransactionCallback<String>>any()))
                .thenAnswer(inv -> {
                    org.springframework.transaction.support.TransactionCallback<String> cb = inv.getArgument(0);
                    return cb.doInTransaction(null);
                });

        ForgotPasswordResponse response = passwordResetService.requestReset("john@example.com");

        assertThat(response.getMessage()).contains("Si el correo esta registrado");

        verify(tokenRepository).deleteByUser(sampleUser);
        ArgumentCaptor<PasswordResetToken> captor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(tokenRepository).save(captor.capture());
        assertThat(captor.getValue().getUser()).isEqualTo(sampleUser);
        assertThat(captor.getValue().isUsed()).isFalse();

        verify(emailService).sendPasswordResetEmail(eq("john@example.com"), contains("reset-password.html?token="));
    }

    @Test
    @DisplayName("requestReset: email inexistente - misma respuesta generica")
    void requestReset_unknownEmail_sameMessage() {
        when(userRepository.findByEmail("nobody@example.com")).thenReturn(Optional.empty());

        ForgotPasswordResponse response = passwordResetService.requestReset("nobody@example.com");

        assertThat(response.getMessage()).contains("Si el correo esta registrado");
        verify(tokenRepository, never()).save(any());
        verify(emailService, never()).sendPasswordResetEmail(any(), any());
    }

    @Test
    @DisplayName("resetPassword: token valido - actualiza contrasena")
    void resetPassword_validToken_updatesPassword() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("abc-123")
                .user(sampleUser)
                .expiresAt(LocalDateTime.now().plusMinutes(30))
                .used(false)
                .build();

        when(tokenRepository.findByTokenAndUsedFalse("abc-123")).thenReturn(Optional.of(token));
        when(passwordEncoder.encode("newsecret")).thenReturn("NEW_HASH");

        MessageResponse response = passwordResetService.resetPassword("abc-123", "newsecret");

        assertThat(response.getMessage()).contains("actualizada");
        assertThat(sampleUser.getPassword()).isEqualTo("NEW_HASH");
        assertThat(token.isUsed()).isTrue();
        verify(userRepository).save(sampleUser);
        verify(tokenRepository).save(token);
    }

    @Test
    @DisplayName("resetPassword: token invalido - lanza excepcion")
    void resetPassword_invalidToken_throws() {
        when(tokenRepository.findByTokenAndUsedFalse("bad")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> passwordResetService.resetPassword("bad", "newsecret"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("invalido");
    }

    @Test
    @DisplayName("resetPassword: token expirado - lanza excepcion")
    void resetPassword_expiredToken_throws() {
        PasswordResetToken token = PasswordResetToken.builder()
                .token("expired")
                .user(sampleUser)
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .used(false)
                .build();

        when(tokenRepository.findByTokenAndUsedFalse("expired")).thenReturn(Optional.of(token));

        assertThatThrownBy(() -> passwordResetService.resetPassword("expired", "newsecret"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("expirado");
    }
}
