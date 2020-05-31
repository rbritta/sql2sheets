#
# Build stage
#
FROM maven:3.6.3-openjdk-14-slim AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:14-jdk-slim

RUN groupadd -g 1028 spring && \
    useradd -r -u 1028 spring -g spring

RUN apt-get update && \
    apt-get install -y wget && \
    rm -rf /var/lib/apt/lists/*

USER spring:spring

COPY --chown=spring:spring --from=build /home/app/target/*.jar /sql2sheets/app.jar

WORKDIR /sql2sheets

EXPOSE 8080

HEALTHCHECK --interval=3s --timeout=3s --retries=5 \
    CMD wget --quiet --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-cp", "app.jar", "-Dloader.path=/sql2sheets/lib", "org.springframework.boot.loader.PropertiesLauncher"]