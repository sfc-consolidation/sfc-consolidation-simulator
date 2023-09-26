## SFC Consolidation Simulator

This is a simulator for the SFC Consolidation.

## Dependencies

- Java 17
- Gradle 7.3.3
- Swagger Code generator 3.0.46
- Library
  - [build.gradle](./app/build.gradle)
  - ⚠ Latest version of CloudSimPlus is not updated to Maven Central yet. So, you need to install it manually.
    - Our version: [CloudSimPlus, commit - 42b131a](https://github.com/cloudsimplus/cloudsimplus/tree/42b131a397f00ca587c580b05f6bb1ef43698230)
    - And you must move jar file to `app/libs/cloudsimplus-8.5.0.jar`.

## How to Run

```bash
$ git clone https://github.com/cloudsimplus/cloudsimplus
$ cd cloudsimplus
$ git checkout 42b131a397f00ca587c580b05f6bb1ef43698230
# Do build with Gradle or Maven
# And move .jar file to app/libs/cloudsimplus-8.5.0.jar
$ cd sfc-consolidation-simulator
$ ./gradlew run
```

## How to Dev

### Add new API / Modify API
```bash
$ wget https://repo1.maven.org/maven2/io/swagger/codegen/v3/swagger-codegen-cli/3.0.46/swagger-codegen-cli-3.0.46.jar -O swagger-codegen-cli.jar
$ tar -xvf v3.0.46.tar.gz
$ java -jar app/libs/swagger-codegen-cli.jar generate \
-l java \
-o app \
-i <YOUR-API Server URL> \
--library retrofit2 \
--model-package sfc.consolidation.simulator.generated.model \
--api-package sfc.consolidation.simulator.generated.api
```