package com.perm_tourism.backend.service.impl;

import com.perm_tourism.backend.dto.LoginRequestDto;
import com.perm_tourism.backend.dto.LoginResponseDto;
import com.perm_tourism.backend.dto.UserRegistrationDto;
import com.perm_tourism.backend.dto.UserResponseDto;
import com.perm_tourism.backend.model.User;
import com.perm_tourism.backend.repository.UserRepository;
import com.perm_tourism.backend.service.UserService;
import com.perm_tourism.backend.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto register(UserRegistrationDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Пользователь с таким email уже существует");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setBirthdayDate(dto.getBirthdayDate());
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToResponseDto(savedUser);
    }

    @Override
    public Optional<UserResponseDto> getUserById(Long id) {
      // Ищем только активных пользователей (не удалённых)
      return userRepository.findByIdAndDeletedAtIsNull(id)
          .map(this::mapToResponseDto);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
      // Получаем только активных пользователей
      return userRepository.findAllByDeletedAtIsNull().stream()
          .map(this::mapToResponseDto)
          .collect(Collectors.toList());
    }

    @Override
    public Optional<UserResponseDto> updateUser(Long id, UserRegistrationDto dto) {
      // Ищем только активного пользователя
      return userRepository.findByIdAndDeletedAtIsNull(id)
          .map(user -> {
            // Обновляем имя (если передано)
            if (dto.getName() != null && !dto.getName().isEmpty()) {
              user.setName(dto.getName());
            }

            // Обновляем email (с проверкой уникальности)
            if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
              if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new RuntimeException("Пользователь с таким email уже существует");
              }
              user.setEmail(dto.getEmail());
            }

            // Обновляем дату рождения (если передана)
            if (dto.getBirthdayDate() != null) {
              user.setBirthdayDate(dto.getBirthdayDate());
            }

            // Обновляем пароль (только если передан не пустой и отличается от текущего)
            // TODO: отдельный метод для смены пароля с подтверждением
            if (dto.getPassword() != null && !dto.getPassword().trim().isEmpty()) {
              if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
              }
            }

            user.setUpdatedAt(LocalDateTime.now());

            return userRepository.save(user);
          })
          .map(this::mapToResponseDto);
    }

    @Override
    public boolean deleteUser(Long id) {
      // Ищем только активного пользователя
      return userRepository.findByIdAndDeletedAtIsNull(id)
          .map(user -> {
            user.setDeletedAt(LocalDateTime.now());
            userRepository.save(user);
            return true;
          })
          .orElse(false);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
      // При логине также проверяем, что пользователь не удалён
      User user = userRepository.findByEmailAndDeletedAtIsNull(request.getEmail())
          .orElseThrow(() -> new RuntimeException("Пользователь не найден или удалён"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        String token = jwtUtil.generateToken(user.getEmail());

        user.setAccessToken(token);
        user.setAuthKey(token);
        userRepository.save(user);

        return new LoginResponseDto(token, token);
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
