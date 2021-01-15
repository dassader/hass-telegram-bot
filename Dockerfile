FROM openjdk:8
WORKDIR /build
COPY . ./
RUN ./mvnw package
WORKDIR /data
CMD ["bash"]