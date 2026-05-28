package com.gestix.service;

import com.gestix.dto.UpdateProfileRequest;
import com.gestix.dto.UserProfileResponse;
import com.gestix.entity.User;
import com.gestix.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserProfileResponse getProfile(String email) {
        return toResponse(getUserByEmail(email));
    }

    public UserProfileResponse updateProfile(UpdateProfileRequest request, String email) {
        User user = getUserByEmail(email);
        String newUsername = request.getUsername().trim();

        if (!user.getUsername().equals(newUsername)
                && userRepository.existsByUsername(newUsername)) {
            throw new IllegalArgumentException("El nombre de usuario ya esta en uso");
        }

        user.setUsername(newUsername);
        return toResponse(userRepository.save(user));
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
    }

    private UserProfileResponse toResponse(User user) {
        return UserProfileResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }
}
