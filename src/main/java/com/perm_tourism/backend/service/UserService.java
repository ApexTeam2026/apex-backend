package com.perm_tourism.backend.service;

import com.perm_tourism.backend.dto.LoginRequestDto;
import com.perm_tourism.backend.dto.LoginResponseDto;
import com.perm_tourism.backend.dto.UserRegistrationDto;
import com.perm_tourism.backend.dto.UserResponseDto;
import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto register(UserRegistrationDto dto); // Регистрация нового пользователя

    UserResponseDto getCurrentUser(String token); // Получить данные пользователя по токену

    Optional<UserResponseDto> getUserById(Long id); // Получить пользователя по ID

    List<UserResponseDto> getAllUsers(); // Получить всех пользователей (для админа)

    Optional<UserResponseDto> updateUser(Long id, UserRegistrationDto dto); // Обновить данные пользователя

    boolean deleteUser(Long id); // Удалить пользователя

    LoginResponseDto login(LoginRequestDto request); // Сгенерировать токен при входе в приложение

    void changePassword(Long userId, String oldPassword, String newPassword); // Метод для смены пароля с подтверждением
}
