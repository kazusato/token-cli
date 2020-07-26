# usage

## build

This tool can be built with Gradle.
The shadowJar task generates an all-in-one jar with dependencies.

```
$ ./gradlew clean build shadowJar
```

## run

The storekey command generates an encryption key and store it in the ~/.gentoken file.
This file will be created with a permission of 600 (read/write by the owner only).
If there is a file with the name, this tool will exit with an error.

The generate command generates a JWT token and write it to the standard output.
The days to expire is a mandatory option (which can be specified with --days or -d).

```
$ java -jar build/libs/token-cli-0.1.0-all.jar storekey
$ java -jar build/libs/token-cli-0.1.0-all.jar generate --days 365
```
