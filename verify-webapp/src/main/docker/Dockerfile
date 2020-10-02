FROM openjdk:8-jdk-alpine

WORKDIR /app
ADD maven/verify-webapp-0.0.1-SNAPSHOT.jar verify-app.jar
RUN sh -c 'touch /app/verify-app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/verify-app.jar"]