# 1. Start with a lightweight Java 17 base image
FROM eclipse-temurin:17-jdk-alpine

# 2. Create a restricted group and user (Hardening step!)
RUN addgroup -S spring && adduser -S spring -G spring

# 3. Tell Docker to run all following commands as this new restricted user
USER spring:spring

# 4. Set the working directory inside the container
WORKDIR /app

# 5. Copy the generated JAR file from your machine into the container
COPY target/*.jar app.jar

# 6. Expose the port (CHANGE THIS TO MATCH YOUR SERVICE PORT!)
EXPOSE 8081

# 7. Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]