package com.perm_tourism.backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "place")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Place {

  // Из таблицы
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long placeId;

  @Column(name = "external_id", unique = true, nullable = false)
  private String externalId; // id из Яндекса

  @Column(nullable = false)
  private String name;

  @Column(length = 2000)
  private String description; // Описание места

  @Column(nullable = false)
  private String coordinates;

  @Column(nullable = false)
  private String address;

  @ElementCollection
  @CollectionTable(name = "place_tags", joinColumns = @JoinColumn(name = "place_id"))
  @Column(name = "tag")
  private List<String> tags;

  @Column(nullable = false)
  private String category;

  @Column(precision = 3, scale = 1)
  private Double rate;

  @Column(name = "price_category")
  private String priceCategory;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "update_at")
  private LocalDateTime updateAt;

  // Надо или нет?
  @Column(name = "deleted_at")
  private LocalDateTime deleteAt;


  // Из дизайна и доп Яндекса
  @Column(name = "average_check")
  private Integer averageCheck; //Средний чек

  private String district; //Район

  @Column(name = "working_hours", length = 500)
  private String workingHours;

  private String website;

  @ElementCollection
  @CollectionTable(name = "place_suitable_for", joinColumns = @JoinColumn(name = "place_id"))
  @Column(name = "option")
  private List<String> suitableFor; // Количество людей: один, вдвоем и тд

  @ElementCollection
  @CollectionTable(name = "place_time_of_day", joinColumns = @JoinColumn(name = "place_id"))
  @Column(name = "time")
  private List<String> timeOfDay; // Время суток


  // Ссылки на фотографии
  @Column(name = "photos", columnDefinition = "TEXT")
  private String photos; //JSON-строка: ["url1", "url2", "url3"]

}

