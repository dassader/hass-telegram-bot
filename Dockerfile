FROM openjdk:11.0.9.1-jre
COPY target/app.jar /bot/app.jar
WORKDIR /bot
CMD ["java", "-jar", "app.jar"]