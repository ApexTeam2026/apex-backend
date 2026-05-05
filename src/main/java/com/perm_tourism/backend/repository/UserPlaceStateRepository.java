package com.perm_tourism.backend.repository;

import com.perm_tourism.backend.model.UserPlaceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlaceStateRepository extends JpaRepository<UserPlaceState, Long> {
  //Поиск по user и place
  Optional<UserPlaceState> findByUserIdAndPlaceId(Long userId, Long placeId);

  // Поиск всех избранных мест пользователя
  List<UserPlaceState> findByUserIdAndIsFavoriteTrue(Long userId);

  //Поиск всех посещённых мест пользователя
  List<UserPlaceState> findByUserIdAndIsVisitedTrue(Long userId);

  // Проверка существования связи user + place
  boolean existsByUserIdAndPlaceId(Long userId, Long placeId);
}
