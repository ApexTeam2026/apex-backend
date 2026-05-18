package com.perm_tourism.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class PlaceUpdateDto {
  private String name;
  private String description;
  private String coordinates;
  private String address;
  private List<String> tags;
  private String category;
  private Double rate;
  private String priceCategory;
  private Integer averageCheck;
  private String district;
  private String workingHours;
  private String website;
  private List<String> suitableFor;
  private List<String> timeOfDay;
}
