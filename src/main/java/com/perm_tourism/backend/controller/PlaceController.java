package com.perm_tourism.backend.controller;

import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {
  private final PlaceService placeService;

    // Получение всех мест (с фильтрацией и сортировкой)
    @GetMapping
    public ResponseEntity<List<Place>> getAllPlaces(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String district,
            @RequestParam(defaultValue = "name") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
      List<Place> places = placeService.getAllPlaces(category, tag, district, sortBy, sortDir);
      return ResponseEntity.ok(places);
    }

    // Получение места по ID
    @GetMapping("/{id}")
    public ResponseEntity<Place> getPlaceById(@PathVariable Long id) {
      try {
        Place place = placeService.getPlaceById(id);
        return ResponseEntity.ok(place);
      } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
      }
    }

  // Добавление места по адресу (через Яндекс API)
  @PostMapping("/add-by-address")
  public ResponseEntity<?> addPlaceByAddress(@RequestParam String address) {
    try {
      Place saved = placeService.addPlaceFromAddress(address);
      return new ResponseEntity<>(saved, HttpStatus.CREATED);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
  }

  // Удаление места по ID
  @DeleteMapping("/{id}")
  public  ResponseEntity<Void> deletePlace(@PathVariable Long id) {
    try {
      placeService.deletePlace(id);
      return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
      return ResponseEntity.notFound().build();
    }
  }
}
