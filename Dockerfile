FROM gradle:8.7.0-jdk21 AS builder

WORKDIR /app
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
RUN gradle dependencies --no-daemon || return 0

COPY . .

RUN ./gradlew clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]