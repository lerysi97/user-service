FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /build/common-events
COPY ../common-events/pom.xml .
COPY ../common-events/src ./src
RUN mvn install -DskipTests

WORKDIR /build/user-service
COPY ../user-service/pom.xml .
COPY ../user-service/src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /build/user-service/target/*.jar app.jar
COPY user-service/src/main/resources/init.sql /docker-entrypoint-initdb.d/
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]