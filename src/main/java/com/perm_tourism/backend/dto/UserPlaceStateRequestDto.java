package com.perm_tourism.backend.dto;

import lombok.Data;

@Data
public class UserPlaceStateRequestDto {
  private Long userId;
  private Long placeId;
  private Boolean isFavorite;
  private Boolean isVisited;
}
