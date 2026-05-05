package com.perm_tourism.backend.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserPlaceStateResponseDto {
  private Long id;
  private Long userId;
  private String userName;
  private Long placeId;
  private String placeName;
  private Boolean isFavorite;
  private Boolean isVisited;
  private LocalDateTime visitedAt;
}
