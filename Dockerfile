FROM ubuntu:latest
LABEL authors="zsuzsannamakara"
FROM openjdk:17
WORKDIR /app
COPY target/order-service.jar notification-service.jar
ENTRYPOINT ["java", "-jar", "notification-service.jar"]