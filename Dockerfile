# Stage 1: Cache Gradle dependencies
FROM gradle:latest AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home

# Copy Gradle configuration files and dependencies
COPY build.gradle.* gradle.properties /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app

# Pre-fetch dependencies to optimize build time
RUN gradle clean build -i --stacktrace

# Stage 2: Build Application
FROM gradle:latest AS build

# Copy cached Gradle dependencies
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY . /usr/src/app/
WORKDIR /usr/src/app

# Ensure permissions are correct for the gradle build process
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

# Build the Fat JAR
RUN gradle buildFatJar --no-daemon # Build the fat JAR

# Stage 3: Create the Runtime Image
FROM eclipse-temurin:21-jre AS runtime

# Expose the Ranking API port
EXPOSE 8080

# Create a directory for the app
RUN mkdir /app

# Copy the Fat JAR built from the previous stage
COPY --from=build /home/gradle/src/build/libs/*.jar /app/ranking-api-1.0.0.jar

# Copy the OpenAPI specification file to the runtime image
COPY --from=build /home/gradle/src/specs/ranking-api.openapi.yml /specs/ranking-api.openapi.yml

ENTRYPOINT ["java","-jar","/app/ranking-api-1.0.0.jar"]
