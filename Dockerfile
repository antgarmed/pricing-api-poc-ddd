# ---------- Build stage ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml ./
COPY src ./src
RUN mvn -q -DskipTests package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
USER app
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
ENV JAVA_TOOL_OPTIONS="-Duser.timezone=Europe/Madrid"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
