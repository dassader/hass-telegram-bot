FROM openjdk:8
WORKDIR /build
COPY . ./
RUN ./gradlew --parallel --no-daemon assemble \
&& cp /build/build/libs/app.jar /app.jar \
&& rm -rf /build
WORKDIR /
CMD ["bash"]