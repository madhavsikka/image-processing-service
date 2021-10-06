FROM adoptopenjdk/openjdk11:alpine-jre
COPY target/processor-0.0.1-SNAPSHOT.jar processor-0.0.1-SNAPSHOT.jar
EXPOSE 8090
ENTRYPOINT ["java","-jar","/processor-0.0.1-SNAPSHOT.jar"]