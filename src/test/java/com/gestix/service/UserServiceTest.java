package com.gestix.service;

import com.gestix.dto.UpdateProfileRequest;
import com.gestix.dto.UserProfileResponse;
import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Pruebas unitarias")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .username("johndoe")
                .email("john@example.com")
                .password("HASHED")
                .build();
    }

    @Test
    @DisplayName("getProfile: devuelve username y email")
    void getProfile_success() {
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));

        UserProfileResponse profile = userService.getProfile("john@example.com");

        assertThat(profile.getUsername()).isEqualTo("johndoe");
        assertThat(profile.getEmail()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("updateProfile: actualiza username")
    void updateProfile_success() {
        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setUsername("johnny");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("johnny")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserProfileResponse profile = userService.updateProfile(req, "john@example.com");

        assertThat(profile.getUsername()).isEqualTo("johnny");
    }

    @Test
    @DisplayName("updateProfile: username duplicado lanza excepción")
    void updateProfile_duplicateUsername_throws() {
        UpdateProfileRequest req = new UpdateProfileRequest();
        req.setUsername("taken");

        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("taken")).thenReturn(true);

        assertThatThrownBy(() -> userService.updateProfile(req, "john@example.com"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("El nombre de usuario ya esta en uso");
    }
}
