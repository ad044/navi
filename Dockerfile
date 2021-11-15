#build
FROM maven:3.6.0-jdk-11-slim AS maven
WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn -f pom.xml clean compile assembly:single

#run
#this image is only for amd64 and arm/v8, for arm/v7 use 11-jre-slim-stretch
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=maven /app/target/navi-1.0-jar-with-dependencies.jar navi.jar
COPY .env .
CMD ["java", "-jar", "navi.jar"]
