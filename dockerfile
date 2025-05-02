# Start from a Java 17 runtime image
FROM eclipse-temurin:17-jre-jammy

# Where the app JAR will live inside the container
WORKDIR /app

# Copy the built JAR from your Maven build into the image
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

# Expose the port your Spring Boot app listens on
EXPOSE 8080

# Run the JAR
ENTRYPOINT ["java","-jar","/app/app.jar"]
