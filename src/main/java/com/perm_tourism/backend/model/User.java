package com.perm_tourism.backend.model;

import com.perm_tourism.backend.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userID;

  @Column(nullable = false)
  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender = Gender.NOT_SPECIFIED;

  @Column(name = "birthday_date")
  private LocalDate birthdayDate;

  @Column(name = "access_level")
  private Integer accessLevel = 1; // 1 - обычный пользователь, 2 - модератор, 3 - админ

  @Column(name = "access_token", length = 500)
  private String accessToken;

  @Column(name = "auth_key", length = 500)
  private String authKey;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "deleted_at")
  private LocalDateTime deletedAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
    updatedAt = LocalDateTime.now();
    if (accessLevel == null) {
      accessLevel = 1;
    }
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
