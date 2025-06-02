# Build stage
FROM gradle:8.5-jdk17-alpine AS build
LABEL stage=build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -x test

# Runtime stage
FROM openjdk:17-slim
RUN apt update && apt install jq curl -y && apt clean && rm -rf /var/lib/apt/lists/*
EXPOSE 8082
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]