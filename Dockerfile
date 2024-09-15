FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar /app/ekalavya.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/ekalavya.jar", "--spring.profiles.active=aws"]