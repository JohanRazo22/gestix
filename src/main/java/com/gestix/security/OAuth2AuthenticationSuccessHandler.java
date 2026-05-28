package com.gestix.security;

import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil                  jwtUtil;
    private final UserRepository           userRepository;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = resolveEmail(oAuth2User);

        if (email == null || email.isBlank()) {
            getRedirectStrategy().sendRedirect(request, response, "/login.html?error=oauth2");
            return;
        }

        // Cargar detalles del usuario y generar JWT
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtUtil.generateToken(userDetails);

        // Obtener el username registrado en BD
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Usuario OAuth2 no encontrado en BD"));

        String encodedUsername = URLEncoder.encode(user.getUsername(), StandardCharsets.UTF_8);

        // Redirigir al dashboard con el token y username en los parámetros
        String redirectUrl = "/dashboard.html?token=" + token + "&username=" + encodedUsername;
        getRedirectStrategy().sendRedirect(request, response, redirectUrl);
    }

    /**
     * Resuelve el email del principal cubriendo los distintos proveedores:
     *   Google  → atributo "email"
     *   Microsoft (cuentas personales) → "email"
     *   Microsoft (cuentas work/school) → "preferred_username" o "upn"
     */
    private String resolveEmail(OAuth2User user) {
        String email = user.getAttribute("email");
        if (isPresent(email)) return email;
        String preferred = user.getAttribute("preferred_username");
        if (isPresent(preferred) && preferred.contains("@")) return preferred;
        return user.getAttribute("upn");
    }

    private static boolean isPresent(String s) {
        return s != null && !s.isBlank();
    }
}
