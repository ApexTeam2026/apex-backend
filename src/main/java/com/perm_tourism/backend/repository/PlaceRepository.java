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

  // Поиск по тегу (все места, у которых в списке есть указанный тег)
  @Query("SELECT p FROM Place p WHERE :tag MEMBER OF p.tags")
  List<Place> findByTag(@Param("tag") String tag, Sort sort);

  // Поиск по списку тегов (все места, у которых есть хотя бы один из переданных тегов)
  @Query("SELECT DISTINCT p FROM Place p JOIN p.tags t WHERE t IN :tags")
  List<Place> findByTagsIn(@Param("tags") List<String> tags);
}