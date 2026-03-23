package com.perm_tourism.backend.repository;


import com.perm_tourism.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email); // Поиск пользователя по email
    boolean existsByEmail(String email); // Проверка, существует ли пользователь с таким email
}
