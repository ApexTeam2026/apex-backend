<div align="center">

# /Пермь — Backend API

### Серверная часть мобильного гида по интересным местам города

Обеспечивает авторизацию, управление пользователями, CRUD для мест, избранное, оценки и персонализированные рекомендации.

<br>

![Java](https://img.shields.io/badge/Java-17-007396?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-6DB33F?logo=springboot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-4169E1?logo=postgresql)
![Docker](https://img.shields.io/badge/Docker-20.10-2496ED?logo=docker)
![REST API](https://img.shields.io/badge/API-REST-orange)

</div>

---

## 🚀 О проекте

**/Пермь Backend** — серверная часть мобильного приложения для поиска интересных мест города и получения персонализированных рекомендаций.

API обрабатывает запросы от клиентского приложения, управляет базой данных мест, пользователей, избранного и оценок.

### Основные возможности

- 🔐 **JWT-авторизация** — регистрация, вход, защита эндпоинтов
- 👤 **Управление пользователями** — профиль, обновление данных, мягкое удаление
- 📍 **Управление местами** — CRUD операции, добавление через Яндекс.Геокодер
- ❤️ **Избранное** — добавление/удаление из избранного, синхронизация с сервером
- ✅ **Посещённые места** — хранение истории посещений
- ⭐ **Оценки** — сохранение пользовательских рейтингов
- 🎯 **Рекомендации** — алгоритм подбора мест на основе опроса

---

## 🏗 Архитектура

```text
┌─────────────────────────────────────────────────────────┐
│                    Mobile App (React Native)            │
│                         Frontend                        │
└───────────────────────────┬─────────────────────────────┘
                            │ HTTPS / REST API
                            ▼
┌─────────────────────────────────────────────────────────┐
│                     Spring Boot 2.7                     │
│                       Backend API                       │
├─────────────────────────────────────────────────────────┤
│  Controllers  │  Services  │  Repositories  │  JWT      │
└───────────────────────────┬─────────────────────────────┘
                            │
                            ▼
┌─────────────────────────────────────────────────────────┐
│                      PostgreSQL 15                      │
│                     (Docker Container)                  │
└─────────────────────────────────────────────────────────┘
```

---
## 🛠 Технологический стек

### Backend

| Технология | Назначение |
|------------|------------|
| Java 17 | Основной язык программирования |
| Spring Boot 2.7.18 | Веб-фреймворк для создания REST API |
| Spring Security | Аутентификация и авторизация |
| JWT | Токен-безопасная аутентификация |
| Spring Data JPA / Hibernate | ORM, работа с базой данных |
| PostgreSQL 15 | Реляционная база данных |
| Maven | Сборка и управление зависимостями |
| Docker / Docker Compose | Контейнеризация и оркестрация |
| Swagger (Springdoc OpenAPI) | Документация API |
| Яндекс.Геокодер API | Геокодинг адресов |
| JUnit 4 + Spring Test | Unit-тестирование |
| Lombok | Упрощение кода (геттеры, сеттеры) |

---
## 📂 Структура проекта
```text
apex-backend/
├── src/main/java/com/perm_tourism/backend/
│   ├── controller/              # REST контроллеры
│   │   ├── AuthController.java
│   │   ├── PlaceController.java
│   │   ├── UserController.java
│   │   └── UserPlaceStateController.java
│   ├── service/                 # Бизнес-логика
│   │   ├── UserService.java
│   │   ├── UserPlaceStateService.java
│   │   └── YandexGeocoderService.java
│   ├── service/impl/            # Реализации сервисов
│   ├── repository/              # JPA репозитории
│   ├── model/                   # Сущности БД
│   ├── dto/                     # Объекты передачи данных
│   ├── config/                  # Конфигурации
│   │   ├── SecurityConfig.java
│   │   ├── JwtUtil.java
│   │   └── SwaggerConfig.java
│   └── enums/                   # Перечисления
├── src/main/resources/
│   └── application.properties   # Настройки приложения
├── docker-compose.yml           # Docker Compose
├── Dockerfile                   # Docker образ
└── pom.xml                      # Maven зависимости
```

---

## 🔐 Авторизация

Приложение использует JWT-аутентификацию.

```text
Login Request
      │
      ▼
POST /api/auth/login
      │
      ▼
JWT Access Token
      │
      ▼
Authorization: Bearer <token>
      │
      ▼
Protected Endpoints
```

### Возможности
- JWT токены
- Spring Security фильтры
- Защищённые эндпоинты
- Автоматическая валидация токена
- Централизованная обработка ошибок
  
---

## 🌐 REST API

Все эндпоинты имеют базовый префикс /api. Авторизация осуществляется через JWT-токен.

### Авторизация
```http
POST /api/auth/login
POST /api/users/register
```

### Пользователи
```http
GET    /api/users
GET    /api/users/{id}
PUT    /api/users/{id}
DELETE /api/users/{id}
```

### Места
```http
GET    /api/places
GET    /api/places/{id}
POST   /api/places/add-by-address
DELETE /api/places/{id}
```

### Избранное и посещённые
```http
GET  /api/user-place-state/favorites/{userId}
GET  /api/user-place-state/visited/{userId}
POST /api/user-place-state/favorite
POST /api/user-place-state/visited
```
---

## 📚 Документация API (Swagger)

После запуска приложения документация доступна по адресу:

```text
http://localhost:8081/swagger-ui/index.html
```

### Swagger UI позволяет:
- Просматривать все эндпоинты
- Тестировать запросы прямо в браузере
- Смотреть схемы данных (DTO)
- Копировать примеры запросов

---

## 🐳 Контейнеризация

Полностью контейнеризировано. Приложение и PostgreSQL запускаются в изолированных контейнерах.

### Переменные окружения (.env)
```env
# База данных
POSTGRES_DB=perm_tourism
POSTGRES_USER=appuser
POSTGRES_PASSWORD=password

# Spring Boot
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/perm_tourism
SPRING_DATASOURCE_USERNAME=appuser
SPRING_DATASOURCE_PASSWORD=password
SPRING_JPA_HIBERNATE_DDL_AUTO=update

# Яндекс API (обязательно для геокодинга)
YANDEX_API_KEY=your_yandex_api_key
```

### Docker Compose
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15-alpine
    container_name: perm-tourism-db
    environment:
      POSTGRES_DB: perm_tourism
      POSTGRES_USER: appuser
      POSTGRES_PASSWORD: password
    volumes:
      - postgres-data:/var/lib/postgresql/data

  app:
    build: .
    container_name: perm-tourism-app
    ports:
      - "8081:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/perm_tourism
      SPRING_DATASOURCE_USERNAME: appuser
      SPRING_DATASOURCE_PASSWORD: password
      YANDEX_API_KEY: ${YANDEX_API_KEY}
    depends_on:
      - postgres
```

---

## ⚙️ Установка и запуск

### Требования: 
- Java 17
- Maven 3.8+
- Docker (опционально)

### Клонирование проекта
```bash
git clone https://github.com/ApexTeam2026/apex-backend.git
cd apex-backend
```

### Локальный запуск
```bash
# Сборка
mvn clean package -DskipTests
## Запуск
java -jar target/perm-discovery-map-1.0.0.jar
```

### Запуск через Docker
```bash
# Быстрый запуск
docker-compose up -d
# Перезапуск с пересборкой
docker-compose down && docker-compose up --build
```

### Проверка работоспособности
```bash
curl http://localhost:8081/api/test/health
# Ожидаемый ответ: OK
```

---

## 🧪 Тестирование

```bash
# Запуск всех тестов
mvn test
# Сборка без тестов (быстрее)
mvn clean package -DskipTests
```

---

## 📦 Развёртывание на сервере
1. Подготовка сервера
```bash
# Клонирование репозитория
git clone https://github.com/ApexTeam2026/apex-backend.git
cd apex-backend
```
2. Настройка переменных окружения
```bash
cp .env.example .env
nano .env  # заполнить реальные значения
```
3. Запуск
```bash
docker-compose up -d --build
```
4. Проверка
```bash
curl http://localhost:8081/api/test/health
```

---

Проект разработан в рамках дисциплины "Проектная практика программирования".

---

<div align="center">

### 📍 Открывай новые места вместе с /Пермь

</div>
