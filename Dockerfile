FROM maven:3.9-amazoncorretto-11 AS build
ADD . /build
RUN cd /build && mvn package --quiet -DskipTests

FROM openjdk:11.0-jre
COPY --from=build /build/target/*.jar /app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]