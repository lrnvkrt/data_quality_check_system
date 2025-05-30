FROM gradle:8.5-jdk21-alpine AS builder
WORKDIR /app

COPY build.gradle.kts ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon

COPY . .
RUN gradle bootJar --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime
ENV TZ=UTC

RUN addgroup -S app && adduser -S app -G app && \
    mkdir /app && chown app:app /app
USER app
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

HEALTHCHECK --interval=30s --timeout=5s --start-period=1s CMD wget -qO- http://localhost:8000/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
