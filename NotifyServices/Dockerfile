
FROM openjdk:17-jdk-alpine

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} notifyservice.jar

ENTRYPOINT ["java", "-jar", "/notifyservice.jar"]

EXPOSE 8087






