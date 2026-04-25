package com.perm_tourism.backend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.perm_tourism.backend.dto.PlaceDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class YandexGeocoderService {
  @Value("${yandex.api.key}")
  private String apiKey;

  private final RestTemplate restTemplate = new RestTemplate();
  private final ObjectMapper objectMapper = new ObjectMapper();

  public PlaceDto getPlaceByAddress(String address) {
    // Формируем URL для запроса к Яндекс.Геокодеру
    String url = "https://geocode-maps.yandex.ru/1.x/?apikey=" + apiKey +
        "&geocode=" + address + "&format=json";

    // Отправляем запрос и получаем ответ в виде строки JSON
    String response = restTemplate.getForObject(url, String.class);

    try {
      // Парсим JSON
      JsonNode root = objectMapper.readTree(response);

      // Достаём первый найденный объект (самый релевантный)
      JsonNode geoObject = root.path("response")
          .path("GeoObjectCollection")
          .path("featureMember")
          .get(0)
          .path("GeoObject");

      // Извлекаем данные
      String externalId = geoObject.path("id").asText();

      String name = geoObject.path("name").asText();
      String description = geoObject.path("description").asText();
      String addressText = geoObject.path("boundedBy")
          .path("Envelope")
          .path("lowerCorner").asText();

      // Координаты от Яндекса приходят как "долгота широта"
      String pos = geoObject.path("Point").path("pos").asText();
      String[] coords = pos.split(" ");
      String coordinates = coords[1] + "," + coords[0]; // Широта, долгота

      // Заполняем DTO
      PlaceDto dto = new PlaceDto();
      dto.setId(null); // ID будет присвоен при сохранении в БД
      dto.setExternalId(externalId);
      dto.setName(name);
      dto.setDescription(description);
      dto.setAddress(addressText);
      dto.setCoordinates(coordinates);

      // Остальные поля остаются пустыми — их заполняет администратор
      dto.setTags(null);
      dto.setCategory(null);
      dto.setRate(null);
      dto.setPriceCategory(null);
      dto.setAverageCheck(null);
      dto.setDistrict(null);
      dto.setWorkingHours(null);
      dto.setWebsite(null);
      dto.setSuitableFor(null);
      dto.setTimeOfDay(null);

      return dto;
    } catch (Exception e) {
      throw new RuntimeException("Ошибка при парсинге ответа Яндекс API:" + e.getMessage());
    }
  }
}
