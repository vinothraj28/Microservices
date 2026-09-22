FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY SharedProto ./SharedProto
COPY movie/movie ./movie/movie

RUN mvn -B -ntp -f SharedProto/pom.xml clean install -DskipTests
RUN mvn -B -ntp -f movie/movie/pom.xml clean package -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/movie/movie/target/movie-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8082 9091
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

