package com.gestix.security;

import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String email    = oAuth2User.getAttribute("email");
        String fullName = oAuth2User.getAttribute("name");

        // Crear el usuario si no existe todavía
        userRepository.findByEmail(email).orElseGet(() -> {
            String username = buildUniqueUsername(fullName, email);

            User newUser = User.builder()
                    .username(username)
                    .email(email)
                    // Contraseña aleatoria — los usuarios OAuth2 nunca hacen login con contraseña
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();

            return userRepository.save(newUser);
        });

        return oAuth2User;
    }

    /** Genera un username único basado en el nombre o email de Google */
    private String buildUniqueUsername(String fullName, String email) {
        String base = (fullName != null && !fullName.isBlank())
                ? fullName.replaceAll("\\s+", "").toLowerCase()
                : email.split("@")[0].toLowerCase();

        // Truncar a 45 chars para dejar espacio a sufijos numéricos
        if (base.length() > 45) base = base.substring(0, 45);

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }
}
