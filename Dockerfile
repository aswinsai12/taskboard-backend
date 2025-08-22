FROM eclipse-temurin:21-jdk AS build
WORKDIR /app
COPY . .

#Make Maven Wrapper executable if present; ignore error if absent
RUN chmod +x mvnw || true

#Build with Maven Wrapper; if missing, fall back to system Maven
RUN ./mvnw -q -DskipTests package || mvn -q -DskipTests package

FROM eclipse-temurin:21-jre
WORKDIR /app

Copy the Spring Boot fat JAR from the build stage
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
