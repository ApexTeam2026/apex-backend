package com.perm_tourism.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_place_state",
  uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "place_id"})
  })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPlaceState {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Column(name = "place_id", nullable = false)
  private Long placeId;

  @Column(name = "is_favorite", nullable = false)
  private Boolean isFavorite = false;

  @Column(name = "is_visited", nullable = false)
  private Boolean isVisited = false;

  @CreationTimestamp
  @Column(name = "visited_at")
  private LocalDateTime visitedAt;
}
