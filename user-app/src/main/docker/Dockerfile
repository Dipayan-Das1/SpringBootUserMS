FROM openjdk:8-jdk-alpine

WORKDIR /app
ADD maven/user-app-0.0.1-SNAPSHOT.jar user-app.jar
RUN sh -c 'touch /app/user-app.jar'
ENTRYPOINT ["sh", "-c", "java -Daws.accessKeyId=${AWS_ACCESS_KEY} -Daws.secretKey=${AWS_SECRET_KEY} -jar /app/user-app.jar"]