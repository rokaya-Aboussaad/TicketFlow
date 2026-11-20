FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
COPY services services
RUN --mount=type=cache,target=/root/.m2,sharing=locked \
    mvn -B -ntp -pl services/notification-service -am -DskipTests \
    -Dmaven.wagon.http.retryHandler.count=10 -Dmaven.wagon.httpconnectionManager.ttlSeconds=120 package
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/services/notification-service/target/notification-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8086
ENTRYPOINT ["java","-jar","/app/app.jar"]
