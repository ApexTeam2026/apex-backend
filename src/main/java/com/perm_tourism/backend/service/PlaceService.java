package com.perm_tourism.backend.service;

import com.perm_tourism.backend.dto.PlaceDto;
import com.perm_tourism.backend.dto.PlaceUpdateDto;
import com.perm_tourism.backend.model.Place;
import com.perm_tourism.backend.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceService {
  private final PlaceRepository placeRepository;
  private final YandexGeocoderService yandexGeocoderService;

  public List<Place> getAllPlaces(String category, String tag, String district, String sortBy, String sortDir) {
    Sort.Direction direction = Sort.Direction.fromString(sortDir);
    Sort sort = Sort.by(direction, sortBy);

    if (category != null && !category.isEmpty()) {
      return placeRepository.findByCategory(category, sort);
    } else if (tag != null && !tag.isEmpty()) {
      return placeRepository.findByTag(tag, sort);
    } else if (district != null && !district.isEmpty()) {
      return placeRepository.findByDistrict(district, sort);
    } else {
      return placeRepository.findAll(sort);
    }
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
}

