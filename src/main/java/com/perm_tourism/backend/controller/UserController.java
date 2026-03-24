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

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // Регистрация пользователя
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register (@Valid @RequestBody UserRegistrationDto dto) {
        try {
            UserResponseDto registeredUser = userService.register(dto);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Пользователь с таким email уже существует
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
