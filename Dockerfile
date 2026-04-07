FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy your jar file
COPY UserVerification.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
