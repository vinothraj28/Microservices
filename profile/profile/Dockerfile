FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace

COPY SharedProto ./SharedProto
COPY profile/profile ./profile/profile

RUN mvn -B -ntp -f SharedProto/pom.xml clean install -Dmaven.test.skip=true
RUN mvn -B -ntp -f profile/profile/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/profile/profile/target/profile-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081 9090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
