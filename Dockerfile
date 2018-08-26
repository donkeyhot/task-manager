FROM java:8-jdk
WORKDIR /app
COPY ./webapp /app
CMD ["./gradlew", "test", "bootRun"]
