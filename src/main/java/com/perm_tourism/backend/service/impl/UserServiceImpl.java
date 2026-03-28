package com.perm_tourism.backend.service.impl;

import com.perm_tourism.backend.dto.UserRegistrationDto;
import com.perm_tourism.backend.dto.UserResponseDto;
import com.perm_tourism.backend.model.User;
import com.perm_tourism.backend.repository.UserRepository;
import com.perm_tourism.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponseDto register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // TODO: добавить хеширование
        user.setBirthdayDate(dto.getBirthdayDate());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToResponseDto(savedUser);
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToResponseDto);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDto> updateUser(Long id, UserRegistrationDto dto) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(dto.getName());
                    user.setEmail(dto.getEmail());
                    user.setBirthdayDate(dto.getBirthdayDate());
                    user.setUpdatedAt(LocalDateTime.now());
                    // TODO: отдельный метод для смены пароля с подтверждением и хешированием
                    return userRepository.save(user);
                })
                .map(this::mapToResponseDto);
    }

    @Override
    public boolean deleteUser(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setDeletedAt(LocalDateTime.now());
                    userRepository.save(user);
                    return true;
                })
                .orElse(false);
    }

    // Преобразование User -> UserResponseDto
    private UserResponseDto mapToResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setUserID(user.getUserID());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setBirthdayDate(user.getBirthdayDate());
        // TODO: добавить avatarUrl
        return dto;
    }
}
