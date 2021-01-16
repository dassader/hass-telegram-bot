ARG BUILD_FROM
FROM $BUILD_FROM
WORKDIR /build
COPY . ./
RUN ./gradlew --parallel --no-daemon assemble \
&& cp /build/build/libs/app.jar /app.jar \
&& rm -rf /build
WORKDIR /
CMD ["java", "-jar", "app.jar"]
