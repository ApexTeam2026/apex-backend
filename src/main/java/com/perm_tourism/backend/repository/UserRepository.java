package com.perm_tourism.backend.repository;


import com.perm_tourism.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Поиск пользователя по email
    boolean existsByEmail(String email); // Проверка, существует ли пользователь с таким email

  // Поиск только активных пользователей (не удалённых)
  Optional<User> findByIdAndDeletedAtIsNull(Long id);

  // Поиск всех активных пользователей
  List<User> findAllByDeletedAtIsNull();

  // Проверка существования email у другого пользователя (для обновления)
  boolean existsByEmailAndIdNot(String email, Long id);

  // Поиск активного пользователя по email (не удалён)
  Optional<User> findByEmailAndDeletedAtIsNull(String email);
}
