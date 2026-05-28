package com.gestix.security;

import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Maneja el flujo OIDC (Microsoft).
 * Microsoft no siempre devuelve el atributo `email`; en cuentas de
 * trabajo/escuela el correo viene en `preferred_username` o `upn`.
 */
@Service
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String email = extractEmail(oidcUser);
        String name  = extractName(oidcUser);

        if (!notBlank(email)) {
            throw new OAuth2AuthenticationException(
                    new OAuth2Error("missing_email",
                            "El proveedor OIDC no devolvió un email utilizable.", null));
        }

        final String finalEmail = email;
        final String finalName  = name;

        userRepository.findByEmail(finalEmail).orElseGet(() -> {
            String username = buildUniqueUsername(finalName, finalEmail);
            User newUser = User.builder()
                    .username(username)
                    .email(finalEmail)
                    .password(passwordEncoder.encode(UUID.randomUUID().toString()))
                    .build();
            return userRepository.save(newUser);
        });

        return oidcUser;
    }

    /** email → preferred_username → upn (cubre cuentas personales y de organización). */
    private String extractEmail(OidcUser user) {
        String email = user.getEmail();
        if (notBlank(email)) return email;
        email = user.getAttribute("email");
        if (notBlank(email)) return email;
        email = user.getAttribute("preferred_username");
        if (notBlank(email) && email.contains("@")) return email;
        return user.getAttribute("upn");
    }

    private String extractName(OidcUser user) {
        String name = user.getFullName();
        if (notBlank(name)) return name;
        name = user.getAttribute("name");
        if (notBlank(name)) return name;
        String given  = user.getGivenName();
        String family = user.getFamilyName();
        String composed = ((given  == null ? "" : given) + " " +
                          (family == null ? "" : family)).trim();
        return composed.isEmpty() ? null : composed;
    }

    private String buildUniqueUsername(String fullName, String email) {
        String base = notBlank(fullName)
                ? fullName.replaceAll("\\s+", "").toLowerCase()
                : email.split("@")[0].toLowerCase();

        if (base.length() > 45) base = base.substring(0, 45);

        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }
}
