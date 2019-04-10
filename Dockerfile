FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/lecture-1.0-SNAPSHOT.jar app.jar
EXPOSE 8090
RUN sh -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]

