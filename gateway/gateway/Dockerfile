FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY SharedProto ./SharedProto
COPY gateway/gateway ./gateway/gateway

RUN mvn -B -ntp -f SharedProto/pom.xml clean install -Dmaven.test.skip=true
RUN mvn -B -ntp -f gateway/gateway/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/gateway/gateway/target/gateway-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
