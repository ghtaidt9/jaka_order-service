# --- stage 1: build ---
FROM gradle:9.5.1-jdk17 AS build
WORKDIR /build
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon || true
COPY src ./src
RUN gradle clean build -x test --no-daemon

# --- stage 2: run ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /build/build/libs/order-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
