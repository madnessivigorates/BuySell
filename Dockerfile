# Используем официальный образ OpenJDK как базовый
FROM openjdk:21-jdk-slim

# Указываем рабочую директорию внутри контейнера
WORKDIR /app

# Копируем файл JAR вашего Spring Boot приложения в контейнер
COPY target/BuySell-0.0.1.jar /app/BuySell-0.0.1.jar

# Указываем команду для запуска приложения
CMD ["java", "-jar", "BuySell-0.0.1.jar","-Dspring.profiles.active=docker"]
