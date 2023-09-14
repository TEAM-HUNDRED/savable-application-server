FROM openjdk:17
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} savable.jar
ENTRYPOINT ["java","-jar", "/savable.jar"]
