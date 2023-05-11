#Springboot dockerfile

# Pull base image
FROM eclipse-temurin:17-jdk-alpine
COPY . /app
WORKDIR /app
CMD ["/app/mvnw", "spring-boot:run"]