package com.perm_tourism.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class QuizRequestDto {
  private List<String> tags;
  private List<String> suitableFor;
  private List<String> timeOfDay;
  private List<String> priceCategory;
}
