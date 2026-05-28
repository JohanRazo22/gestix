package com.gestix.config;

import com.gestix.security.CustomOAuth2UserService;
import com.gestix.security.CustomOidcUserService;
import com.gestix.security.CustomUserDetailsService;
import com.gestix.security.JwtAuthFilter;
import com.gestix.security.OAuth2AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter                      jwtAuthFilter;
    private final CustomUserDetailsService           userDetailsService;
    private final CustomOAuth2UserService            oAuth2UserService;
    private final CustomOidcUserService              oidcUserService;
    private final OAuth2AuthenticationSuccessHandler oAuth2SuccessHandler;
    private final PasswordEncoder                    passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/", "/index.html", "/login.html", "/reset-password.html",
                                         "/dashboard.html", "/css/**", "/js/**").permitAll()
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                        .anyRequest().authenticated()
                )
                // IF_REQUIRED: permite que el flujo OAuth2 use sesión internamente
                // para validar el parámetro 'state'. Las llamadas a /api/** siguen
                // siendo autenticadas por JWT sin estado.
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )
                // ── Para llamadas /api/** devolver 401 JSON en vez de redirigir a OAuth2 ──
                .exceptionHandling(ex -> ex
                        .defaultAuthenticationEntryPointFor(
                                (request, response, authException) -> {
                                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                                    response.setContentType("application/json");
                                    response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token inválido o expirado\"}");
                                },
                                new AntPathRequestMatcher("/api/**")
                        )
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                // ── OAuth2 Login (Google + Microsoft) ──────────────────────────────
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(info -> info
                                .userService(oAuth2UserService)    // Google (no openid)
                                .oidcUserService(oidcUserService)  // Microsoft (OIDC)
                        )
                        .successHandler(oAuth2SuccessHandler)
                        .failureUrl("/login.html?error=oauth2")
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
