# Используем официальный образ OpenJDK 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем собранный JAR файл приложения в контейнер
COPY target/mobile-platform-1.0-SNAPSHOT.jar app.jar

# Копируем файл конфигурации в контейнер
COPY src/main/resources/application-docker-do.properties /app/config/

# Открываем порт, на котором работает Spring Boot (80)
EXPOSE 80

# Устанавливаем активный профиль для Docker
#ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_PROFILES_ACTIVE=docker-do

# Команда для запуска приложения
#CMD ["java", "-jar", "app.jar"]
CMD ["java", "-jar", "app.jar", "--spring.config.location=/app/config/application-docker-do.properties"]
