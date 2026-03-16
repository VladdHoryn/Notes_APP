# Використовуємо легкий JDK
FROM eclipse-temurin:17-jdk-alpine

# Робоча директорія в контейнері
WORKDIR /app

# Копіюємо jar файл
COPY target/Technical_task-1.0-SNAPSHOT.jar app.jar

# Порт Spring Boot
EXPOSE 8080

# Команда запуску
ENTRYPOINT ["java", "-jar", "app.jar"]