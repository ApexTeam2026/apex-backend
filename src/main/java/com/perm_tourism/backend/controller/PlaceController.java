package com.perm_tourism.backend.controller;

import com.perm_tourism.backend.dto.PlaceDto;
import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.repository.PlaceRepository;
import com.perm_tourism.backend.service.YandexGeocoderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {
  private final YandexGeocoderService yandexGeocoderService;
  private final PlaceRepository placeRepository;

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
