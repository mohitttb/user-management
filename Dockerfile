FROM openjdk:17-alpine
WORKDIR /app
COPY ./target/user-management-0.0.1-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]