package com.perm_tourism.backend.controller;

import com.perm_tourism.backend.dto.PlaceDto;
import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.repository.PlaceRepository;
import com.perm_tourism.backend.service.YandexGeocoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {
  private final YandexGeocoderService yandexGeocoderService;
  private final PlaceRepository placeRepository;

    // Получение всех мест (с фильтрацией и сортировкой)
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String district,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDir);
        Sort sort = Sort.by(direction, sortBy);

        List<Place> places;

        if (category != null && !category.isEmpty()) {
            places = placeRepository.findByCategory(category, sort);
        } else if (tag != null && !tag.isEmpty()) {
            places = placeRepository.findByTag(tag, sort);
        } else if (district != null && !district.isEmpty()) {
            places = placeRepository.findByDistrict(district, sort);
        } else {
            places = placeRepository.findAll(sort);
        }

        return ResponseEntity.ok(places);
    }

    // Получение места по ID
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
        return placeRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

  // Добавление места по адресу (через Яндекс API)
  @PostMapping("/add-by-address")
  public ResponseEntity<?> addPlaceByAddress(@RequestParam String address) {
    try {
      // Получаем данные от Яндекс API
      PlaceDto dto = yandexGeocoderService.getPlaceByAddress(address);

      // Создаём новое место
      Place place = new Place();
      place.setExternalId(dto.getExternalId());
      place.setName(dto.getName());
      place.setDescription(dto.getDescription());
      place.setCoordinates(dto.getCoordinates());
      place.setAddress(dto.getAddress());

      // Временно пустые значения — потом можно будет редактировать
      place.setTags(Collections.emptyList());
      place.setCategory("");
      place.setRate(null);
      place.setPriceCategory("");
      place.setAverageCheck(null);
      place.setDistrict("");
      place.setWorkingHours("");
      place.setWedsite("");
      place.setSuitableFor(Collections.emptyList());
      place.setTimeOfDay(Collections.emptyList());

      // Сохраняем в базу
      Place saved = placeRepository.save(place);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
  }

  // Удаление места по ID
  @DeleteMapping("/{id}")
  public  ResponseEntity<Void> deletePlace(@PathVariable Long id) {
    if (!placeRepository.existsById(id)) {
      return ResponseEntity.notFound().build();
    }
    placeRepository.deleteById(id);
    return ResponseEntity.noContent().build();
  }
}
