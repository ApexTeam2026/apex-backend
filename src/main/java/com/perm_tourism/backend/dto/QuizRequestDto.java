package com.perm_tourism.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequestDto {
  private List<String> tags;
}
