package com.perm_tourism.backend.service;

import com.perm_tourism.backend.dto.PlaceDto;
import com.perm_tourism.backend.dto.PlaceUpdateDto;
import com.perm_tourism.backend.dto.QuizRequestDto;
import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;
  private final YandexGeocoderService yandexGeocoderService;

  public List<Place> getAllPlaces(List<String> categories, List<String> districts,
                                  Integer avgCheckMin, Integer avgCheckMax,
                                  List<String> timeOfDay, List<String> suitableFor,
                                  String sortBy, String sortDir) {

    Sort sort = Sort.by(Sort.Direction.fromString(sortDir), sortBy);
    List<Place> places = placeRepository.findAll(sort);

    // Фильтр по категориям
    if (categories != null && !categories.isEmpty()) {
      places = places.stream()
        .filter(p -> categories.contains(p.getCategory()))
        .collect(Collectors.toList());
    }

    // Фильтр по районам
    if (districts != null && !districts.isEmpty()) {
      places = places.stream()
        .filter(p -> p.getDistrict() != null && districts.contains(p.getDistrict()))
        .collect(Collectors.toList());
    }

    // Фильтр по среднему чеку
    if (avgCheckMin != null || avgCheckMax != null) {
      places = places.stream()
        .filter(p -> {
          Integer check = p.getAverageCheck();
          if (check == null) return false;
          if (avgCheckMin != null && check < avgCheckMin) return false;
          if (avgCheckMax != null && check > avgCheckMax) return false;
          return true;
        })
        .collect(Collectors.toList());
    }

    // Фильтр по времени посещения
    if (timeOfDay != null && !timeOfDay.isEmpty()) {
      places = places.stream()
        .filter(p -> p.getTimeOfDay() != null &&
          p.getTimeOfDay().stream().anyMatch(timeOfDay::contains))
        .collect(Collectors.toList());
    }

    // Фильтр по количеству людей
    if (suitableFor != null && !suitableFor.isEmpty()) {
      places = places.stream()
        .filter(p -> p.getSuitableFor() != null &&
          p.getSuitableFor().stream().anyMatch(suitableFor::contains))
        .collect(Collectors.toList());
    }

    return places;
  }

  public Place getPlaceById(Long id) {
    return placeRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Place not found: " + id));
  }

  public Place addPlaceFromAddress(String address) {
    PlaceDto dto = yandexGeocoderService.getPlaceByAddress(address);

    String externalId = dto.getExternalId();
    if (externalId == null || externalId.isEmpty()) {
      externalId = "yandex_fallback_" + UUID.randomUUID();
    }

    Place place = new Place();
    place.setExternalId(externalId);
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
    place.setWebsite("");
    place.setSuitableFor(Collections.emptyList());
    place.setTimeOfDay(Collections.emptyList());

    return placeRepository.save(place);
  }

  public Place updatePlace(Long id, PlaceUpdateDto dto) {
    Place place = placeRepository.findById(id)
      .orElseThrow(() -> new RuntimeException("Place not found: " + id));

    if (dto.getName() != null) place.setName(dto.getName());
    if (dto.getDescription() != null) place.setDescription(dto.getDescription());
    if (dto.getCoordinates() != null) place.setCoordinates(dto.getCoordinates());
    if (dto.getAddress() != null) place.setAddress(dto.getAddress());
    if (dto.getTags() != null) place.setTags(dto.getTags());
    if (dto.getCategory() != null) place.setCategory(dto.getCategory());
    if (dto.getRate() != null) place.setRate(dto.getRate());
    if (dto.getPriceCategory() != null) place.setPriceCategory(dto.getPriceCategory());
    if (dto.getAverageCheck() != null) place.setAverageCheck(dto.getAverageCheck());
    if (dto.getDistrict() != null) place.setDistrict(dto.getDistrict());
    if (dto.getWorkingHours() != null) place.setWorkingHours(dto.getWorkingHours());
    if (dto.getWebsite() != null) place.setWebsite(dto.getWebsite());
    if (dto.getSuitableFor() != null) place.setSuitableFor(dto.getSuitableFor());
    if (dto.getTimeOfDay() != null) place.setTimeOfDay(dto.getTimeOfDay());

    return placeRepository.save(place);
  }

  public void deletePlace(Long id) {
    if (!placeRepository.existsById(id)) {
      throw new RuntimeException("Place not found: " + id);
    }
    placeRepository.deleteById(id);
  }

  public List<Long> getPlaceIdsByQuiz(QuizRequestDto request) {
    List<String> tags = request.getTags();
    List<String> suitableFor = request.getSuitableFor();
    List<String> timeOfDay = request.getTimeOfDay();
    List<String> priceCategory = request.getPriceCategory();

    List<Place> places;
    if (tags != null && !tags.isEmpty()) {
      places = placeRepository.findByTagsIn(tags);
    } else {
      places = placeRepository.findAll();
    }

    if (suitableFor != null && !suitableFor.isEmpty() && !suitableFor.contains("any")) {
      places = places.stream()
        .filter(p -> p.getSuitableFor() != null &&
          p.getSuitableFor().stream().anyMatch(suitableFor::contains))
        .collect(Collectors.toList());
    }

    if (timeOfDay != null && !timeOfDay.isEmpty() && !timeOfDay.contains("any")) {
      places = places.stream()
        .filter(p -> p.getTimeOfDay() != null &&
          p.getTimeOfDay().stream().anyMatch(timeOfDay::contains))
        .collect(Collectors.toList());
    }

    if (priceCategory != null && !priceCategory.isEmpty() && !priceCategory.contains("any")) {
      places = places.stream()
        .filter(p -> p.getPriceCategory() != null &&
          priceCategory.contains(p.getPriceCategory()))
        .collect(Collectors.toList());
    }

    // Сортируем: чем больше тегов совпало, тем выше место в списке
    if (tags != null && !tags.isEmpty()) {
      places.sort((p1, p2) -> {
        long c1 = p1.getTags().stream().filter(tags::contains).count();
        long c2 = p2.getTags().stream().filter(tags::contains).count();
        return Long.compare(c2, c1);
      });
    }

    return places.stream()
      .map(Place::getPlaceId)
      .collect(Collectors.toList());
  }
}

