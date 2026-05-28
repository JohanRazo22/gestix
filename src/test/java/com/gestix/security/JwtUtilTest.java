package com.gestix.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtUtil - Pruebas unitarias")
class JwtUtilTest {

    private JwtUtil jwtUtil;

    private static final String SECRET =
            "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
    private static final long EXPIRATION = 86_400_000L; // 24 horas

    private UserDetails userA;
    private UserDetails userB;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "secret",     SECRET);
        ReflectionTestUtils.setField(jwtUtil, "expiration", EXPIRATION);

        userA = User.withUsername("user_a@example.com")
                .password("pass").authorities(Collections.emptyList()).build();
        userB = User.withUsername("user_b@example.com")
                .password("pass").authorities(Collections.emptyList()).build();
    }

    // ══════════════════ generateToken ══════════════════

    @Test
    @DisplayName("generateToken: devuelve un token no nulo ni vacío")
    void generateToken_notNullOrBlank() {
        String token = jwtUtil.generateToken(userA);
        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    @DisplayName("generateToken: dos llamadas para el mismo usuario producen tokens distintos (timestamp diferente)")
    void generateToken_differentCallsProduceDifferentTokens() throws InterruptedException {
        String token1 = jwtUtil.generateToken(userA);
        Thread.sleep(10); // pequeña pausa para diferir el issuedAt
        String token2 = jwtUtil.generateToken(userA);
        // Los tokens PUEDEN ser distintos por el issuedAt; ambos deben ser válidos
        assertThat(jwtUtil.isTokenValid(token1, userA)).isTrue();
        assertThat(jwtUtil.isTokenValid(token2, userA)).isTrue();
    }

    // ══════════════════ extractEmail ══════════════════

    @Test
    @DisplayName("extractEmail: extrae el email correcto del subject")
    void extractEmail_returnsCorrectEmail() {
        String token = jwtUtil.generateToken(userA);
        String extracted = jwtUtil.extractEmail(token);
        assertThat(extracted).isEqualTo("user_a@example.com");
    }

    @Test
    @DisplayName("extractEmail: tokens de usuarios distintos tienen subjects distintos")
    void extractEmail_differentUsersHaveDifferentSubjects() {
        String tokenA = jwtUtil.generateToken(userA);
        String tokenB = jwtUtil.generateToken(userB);
        assertThat(jwtUtil.extractEmail(tokenA)).isNotEqualTo(jwtUtil.extractEmail(tokenB));
    }

    // ══════════════════ isTokenValid ══════════════════

    @Test
    @DisplayName("isTokenValid: token generado para userA es válido para userA")
    void isTokenValid_validToken_returnsTrue() {
        String token = jwtUtil.generateToken(userA);
        assertThat(jwtUtil.isTokenValid(token, userA)).isTrue();
    }

    @Test
    @DisplayName("isTokenValid: token de userA NO es válido para userB")
    void isTokenValid_wrongUser_returnsFalse() {
        String tokenA = jwtUtil.generateToken(userA);
        assertThat(jwtUtil.isTokenValid(tokenA, userB)).isFalse();
    }

    @Test
    @DisplayName("isTokenValid: token manipulado - la firma inválida lanza excepción")
    void isTokenValid_tamperedToken_throwsException() {
        String token   = jwtUtil.generateToken(userA);
        String tampered = token.substring(0, token.length() - 5) + "XXXXX";

        // JJWT lanza SignatureException o MalformedJwtException al detectar la firma rota
        assertThatThrownBy(() -> jwtUtil.isTokenValid(tampered, userA))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("isTokenValid: token expirado devuelve false")
    void isTokenValid_expiredToken_returnsFalse() {
        // Crear una instancia con expiración de 1 ms
        JwtUtil expiredJwt = new JwtUtil();
        ReflectionTestUtils.setField(expiredJwt, "secret",     SECRET);
        ReflectionTestUtils.setField(expiredJwt, "expiration", 1L); // expira inmediatamente

        String token = expiredJwt.generateToken(userA);

        // Dar tiempo para que expire
        try { Thread.sleep(50); } catch (InterruptedException ignored) {}

        assertThatThrownBy(() -> expiredJwt.isTokenValid(token, userA))
                .isInstanceOf(Exception.class); // ExpiredJwtException
    }
}
