FROM openjdk:8-jdk-alpine

WORKDIR /app

ARG JAR_FILE=build/libs/user-task-service-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]