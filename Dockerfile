FROM eclipse-temurin:21-jdk-jammy
EXPOSE 8080
ARG JAR_FILE=target/Products-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]