package com.perm_tourism.backend.repository;

import com.perm_tourism.backend.model.Place;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Place, Long> {

  // Поиск по категории с сортировкой
  List<Place> findByCategory(String category, Sort sort);

  // Поиск по району с сортировкой
  List<Place> findByDistrict(String district, Sort sort);

  // Поиск по тегу (так как теги хранятся в коллекции)
  @Query("SELECT p FROM Place p WHERE :tag MEMBER OF p.tags")
  List<Place> findByTag(@Param("tag") String tag, Sort sort);
}