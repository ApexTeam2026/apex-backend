package com.perm_tourism.backend.service;

import com.perm_tourism.backend.dto.UserPlaceStateRequestDto;
import com.perm_tourism.backend.dto.UserPlaceStateResponseDto;
import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.model.User;
import com.perm_tourism.backend.model.UserPlaceState;
import com.perm_tourism.backend.repository.PlaceRepository;
import com.perm_tourism.backend.repository.UserPlaceStateRepository;
import com.perm_tourism.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserPlaceStateService {
  private final UserPlaceStateRepository stateRepository;
  private final UserRepository userRepository;
  private final PlaceRepository placeRepository;

  @Transactional
  public UserPlaceStateResponseDto setFavorite(UserPlaceStateRequestDto dto) {
    User user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

    Place place = placeRepository.findById(dto.getPlaceId())
      .orElseThrow(() -> new RuntimeException("Место не найдено"));

    UserPlaceState state = stateRepository.findByUserUserIDAndPlacePlaceId(dto.getUserId(), dto.getPlaceId())
      .orElse(new UserPlaceState());

    if (state.getId() == null) {
      state.setUser(user);
      state.setPlace(place);
    }

    state.setIsFavorite(dto.getIsFavorite());

    if (Boolean.TRUE.equals(dto.getIsVisited())) {
      state.setIsVisited(true);
      state.setVisitedAt(LocalDateTime.now());
    }

    UserPlaceState saved = stateRepository.save(state);
    return mapToResponseDto(saved);
  }

  @Transactional
  public UserPlaceStateResponseDto setVisited(UserPlaceStateRequestDto dto) {
    User user = userRepository.findById(dto.getUserId())
      .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

    Place place = placeRepository.findById(dto.getPlaceId())
      .orElseThrow(() -> new RuntimeException("Место не найдено"));

    UserPlaceState state = stateRepository.findByUserUserIDAndPlacePlaceId(dto.getUserId(), dto.getPlaceId())
      .orElse(new UserPlaceState());

    if (state.getId() == null) {
      state.setUser(user);
      state.setPlace(place);
    }

    state.setIsVisited(dto.getIsVisited());
    if (Boolean.TRUE.equals(dto.getIsVisited())) {
      state.setVisitedAt(LocalDateTime.now());
    }

    if (Boolean.TRUE.equals(dto.getIsFavorite())) {
      state.setIsFavorite(true);
    }

    UserPlaceState saved = stateRepository.save(state);
    return mapToResponseDto(saved);
  }

  public List<UserPlaceStateResponseDto> getUserFavorites(Long userId) {

    return stateRepository.findByUserUserIDAndIsFavoriteTrue(userId)
      .stream()
      .map(this::mapToResponseDto)
      .collect(Collectors.toList());
  }

  public List<UserPlaceStateResponseDto> getUserVisited(Long userId) {

    return stateRepository.findByUserUserIDAndIsVisitedTrue(userId)
      .stream()
      .map(this::mapToResponseDto)
      .collect(Collectors.toList());
  }

  private UserPlaceStateResponseDto mapToResponseDto(UserPlaceState state) {
    UserPlaceStateResponseDto dto = new UserPlaceStateResponseDto();
    dto.setId(state.getId());
    dto.setUserId(state.getUser().getUserID());
    dto.setUserName(state.getUser().getName());
    dto.setPlaceId(state.getPlace().getPlaceId());
    dto.setPlaceName(state.getPlace().getName());
    dto.setIsFavorite(state.getIsFavorite());
    dto.setIsVisited(state.getIsVisited());
    dto.setVisitedAt(state.getVisitedAt());
    dto.setRating(state.getRating());
    return dto;
  }

  public UserPlaceStateResponseDto setRating(Long userId, Long placeId, Integer rating) {
    if (rating == null || rating < 1 || rating > 5) {
      throw new RuntimeException("Оценка должна быть от 1 до 5");
    }

    UserPlaceState state = stateRepository.findByUserUserIDAndPlacePlaceId(userId, placeId)
      .orElse(new UserPlaceState());

    if (state.getId() == null) {
      User user = userRepository.findById(userId)
        .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
      Place place = placeRepository.findById(placeId)
        .orElseThrow(() -> new RuntimeException("Место не найдено"));
      state.setUser(user);
      state.setPlace(place);
    }

    state.setRating(rating);

    if (!Boolean.TRUE.equals(state.getIsVisited())) {
      state.setIsVisited(true);
      state.setVisitedAt(LocalDateTime.now());
    }

    return mapToResponseDto(stateRepository.save(state));
  }
}