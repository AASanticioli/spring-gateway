# Builder
FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
VOLUME /root/.gradle
RUN gradle --warning-mode=all --info clean
RUN gradle --build-cache --parallel --stacktrace --warning-mode=all --info bootJar

# Layer
FROM eclipse-temurin:21-jre as layers
WORKDIR /harpy
#COPY ./build/libs/gateway*.jar gateway.jar
COPY --from=build /home/gradle/src/build/libs/*.jar gateway.jar
RUN java -Djarmode=layertools -jar gateway.jar extract

# Run
FROM eclipse-temurin:21-jre-alpine
EXPOSE 8080:8080/tcp
WORKDIR /harpy
COPY --from=layers harpy/dependencies/ ./
COPY --from=layers harpy/spring-boot-loader/ ./
COPY --from=layers harpy/snapshot-dependencies/ ./
COPY --from=layers harpy/application/ ./
RUN apk update && apk upgrade --no-cache
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
