package com.perm_tourism.backend.controller;

import com.perm_tourism.backend.dto.UserRegistrationDto;
import com.perm_tourism.backend.dto.UserResponseDto;
import com.perm_tourism.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody UserRegistrationDto dto) {
        try {
            UserResponseDto registeredUser = userService.register(dto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage())); // Пользователь с таким email уже существует
        }
    }

    // Обновление данных пользователя
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRegistrationDto dto) {
      try {
        return userService.updateUser(id, dto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
      } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
      }
    }

    // Смена пароля с подтверждением
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(@RequestParam Long userId,
                                               @RequestParam String oldPassword,
                                               @RequestParam String newPassword) {
      try {
        userService.changePassword(userId, oldPassword, newPassword);
        return ResponseEntity.ok().build();
      } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(null);
      }
    }

    // Получение пользователя по ID
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Получить всех пользователей
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Удаление пользователя (устанавливается deleteAt - пользователь помечается как удалённый)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (userService.deleteUser(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
