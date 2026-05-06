package com.perm_tourism.backend.controller;

import com.perm_tourism.backend.dto.UserPlaceStateRequestDto;
import com.perm_tourism.backend.dto.UserPlaceStateResponseDto;
import com.perm_tourism.backend.service.UserPlaceStateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-place")
@RequiredArgsConstructor
public class UserPlaceStateController {
  private final UserPlaceStateService service;

  // Добавление/удаление отметки "избранное"
  @PostMapping("/favorite")
  public ResponseEntity<UserPlaceStateResponseDto> setFavorite(@RequestBody UserPlaceStateRequestDto dto) {
    return ResponseEntity.ok(service.setFavorite((dto)));
  }

  // Добавление/удаление отметки "посещено"
  @PostMapping("/visited")
  public ResponseEntity<UserPlaceStateResponseDto> setVisited(@RequestBody UserPlaceStateRequestDto dto) {
    return ResponseEntity.ok((service.setVisited(dto)));
  }

  // Получение всех избранных пользователем мест
  @GetMapping("/favorites/{userId}")
  public ResponseEntity<List<UserPlaceStateResponseDto>> getFavorites(@PathVariable Long userId) {
    return ResponseEntity.ok(service.getUserFavorites(userId));
  }

  // Получение всех посещённых пользователем мест
  @GetMapping("/visited/{userId}")
  public ResponseEntity<List<UserPlaceStateResponseDto>> getVisited(@PathVariable Long userId) {
    return ResponseEntity.ok(service.getUserVisited(userId));
  }
}
