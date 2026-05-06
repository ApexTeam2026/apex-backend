package com.perm_tourism.backend.repository;

import com.perm_tourism.backend.model.UserPlaceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPlaceStateRepository extends JpaRepository<UserPlaceState, Long> {

    // Поиск по user и place
    Optional<UserPlaceState> findByUserUserIDAndPlaceId(Long userId, Long placeId);

    // Поиск всех избранных мест пользователя
    List<UserPlaceState> findByUserUserIDAndIsFavoriteTrue(Long userId);

    // Поиск всех посещённых мест пользователя
    List<UserPlaceState> findByUserUserIDAndIsVisitedTrue(Long userId);

    // Проверка существования связи user + place
    boolean existsByUserUserIDAndPlaceId(Long userId, Long placeId);
}