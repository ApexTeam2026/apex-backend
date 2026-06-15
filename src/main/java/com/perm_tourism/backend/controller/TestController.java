package com.perm_tourism.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

  @GetMapping("/hello")
  public String hello() {
    return "Привет от бэкенда туристического приложения!";
  }

  @GetMapping("/health")
  public String health() {
    return "OK";
  }
}