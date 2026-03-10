# Используем официальный образ Java
FROM eclipse-temurin:17-jdk-jammy

# Рабочая директория внутри контейнера
WORKDIR /app

# Копируем JAR-файл в контейнер
# ВАЖНО: замените "target/demo-0.0.1-SNAPSHOT.jar" на путь к вашему JAR-файлу
COPY target/perm-discovery-map-1.0.0.jar app.jar

# Открываем порт 8080
EXPOSE 8080

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]