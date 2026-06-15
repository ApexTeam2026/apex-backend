package com.perm_tourism.backend.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserResponseDto {
  private Long userID; // для API (не отображаем)
  private String name;
  private String email;
  private LocalDate birthdayDate;
  private String avatarUrl; // ссылка на фото
}
