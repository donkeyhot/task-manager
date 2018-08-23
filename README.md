# Task manager

To build and run this app you should use one of project management tools Maven or Graddle.
Both packages are included in the project structure.

** To run with Maven **

```sh
./mavenw spring-boot:run
```
or

```sh
./mavenw package
java -jar target/task-manager-0.1-SNAPSHOT.jar
```

** To run with Gradle **

```sh
./gradlew bootRun
```

or

```sh
./gradlew build
java -jar build/libs/task-manager-0.1-SNAPSHOT.jar
```

When app started just open http://localhost:8080/index.html with your web-browser.
