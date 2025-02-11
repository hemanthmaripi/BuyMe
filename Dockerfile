FROM openjdk:17-jdk-alpine

WORKDIR /salesavvy

COPY target/CloneProject-0.0.1-SNAPSHOT.jar CloneProject.jar
COPY /src/main/resources/application.properties /app/config/applicaiton.properties

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "CloneProject.jar", "spring.config.location=file:/app/config/applicaiton.properties" ]