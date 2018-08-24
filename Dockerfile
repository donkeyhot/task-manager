FROM java:8-jdk
WORKDIR /app
COPY . /app
CMD ["./gradlew", "bootRun"]

