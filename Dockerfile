FROM java:8-jdk
WORKDIR /app
COPY ./webapp /app
CMD ["./mvnw", "test", "spring-boot:run"]

