FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/status-checker-0.0.1-SNAPSHOT.jar status-checker-0.0.1-SNAPSHOT.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/status-checker-0.0.1-SNAPSHOT.jar"]