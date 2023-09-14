FROM openjdk:17
ARG JAR_FILE=./build/libs/savable-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} savable.jar
ENTRYPOINT ["java","-jar", "/savable.jar"]
