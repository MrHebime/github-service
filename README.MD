# Platform Coding Exercise

## Requirements

```
In Summary 
● Stand up a server 
● Have an endpoint that takes a username 
● Fetch or retrieve the data 
● Return the JSON defined above 
● Have tests to prove your implementation 
Push your finalized code to a public repo (GitHub, BitBucket, GitLab). 
Provide a README explaining your decisions, architecture, and how to install/run and utilize your service

It shouldn't take more than 3-5 hours
```

## How to Build

```shell
# Generate dtos and Swagger docs from openapi.yaml
./gradlew openApiGenerate - 

# Build the project (includes openApiGenerate)
./gradlew build

# Run the tests
./gradlew test

```

## How to Run

```shell
# Run as a Spring Boot App
./gradlew bootRun
```

### View the Swagger documentation for the REST API

```
# HTML docs, you can also test here
http://localhost:8080/swagger-ui/index.html

http:/localhost:8080/swagger-ui.html

http://localhost:8080/v3/api-docs
```

### Get a GitHub user's data using their username

```bash

# http://localhost:8080/github/users/{username}/data
curl -v http://localhost:8080/github/users/octocat/data

```

### Check actuator health endpoints

```
http:/localhost:8081/actuator/health
```

## Architectural Decisions

### OpenAPI

Use OpenAPI for contract-first development. Write the contract in openapi.yaml. Then use the OpenApiGenerator to
generate Java DTO classes and the Swagger UI documentation. If I organized as a multi-module project, we could also have
a java client library that other microservices could use to call this. The Java DTO classes, the Swagger UI, and the
Java client library all stay in sync. The DTO classes allow loose coupling between this microservice's REST API and the
GitHub REST API.

References:
https://plugins.gradle.org/plugin/org.openapi.generator
https://openapi-generator.tech/docs/configuration/
https://swagger.io/docs/specification/v3_0/data-models/data-types/
https://swagger.io/docs/specification/v3_0/describing-parameters/

### MapStruct

Use MapStruct for easy mapping between the objects from the GitHub API and the DTOs generated from the openapi.yaml. The
DTO classes allow loose coupling between this microservice's REST API and the GitHub REST API.

References:
https://mapstruct.org/documentation/

### GitHubService

I wanted GitHubService to be simple with its primary goal of just calling GitHub APIs.

Getting Started: https://docs.github.com/en/rest/guides/getting-started-with-the-rest-api
Data Sources:
https://api.github.com/users/octocat
https://api.github.com/users/octocat/repos

https://docs.github.com/en/rest/users/users?apiVersion=2026-03-10#get-a-user
https://docs.github.com/en/rest/repos/repos?apiVersion=2026-03-10#list-repositories-for-a-user

Using Pagination
https://docs.github.com/en/rest/using-the-rest-api/using-pagination-in-the-rest-api?apiVersion=2026-03-10


## GitHubDataService

Calls GitHubService to get the information from GitHub and then maps it to the DTOs.
This is where I put caching. 
If the user call returns 404, there's no reason to make the repos call so I kept those synchronous.


### Caching

For just this project, I would use Caffeine.
If I were deploying to Kubernetes,  I would use Redis and Caffeine 
to have a distributed cache (Redis) and Caffeine (local)

References:
https://www.baeldung.com/spring-boot-caffeine-cache

### Spring Boot Starter

I used Spring Initializer to get a Spring Boot gradle project to start.
https://start.spring.io/

```
Project: Gradle Groovy
Language: Java 
Spring Boot: 4.1.0
Project Metadata Group: com.example 
Artifact: github-service
Package name: com.example.github-service
Packaging: Jar
Configuration: YAML 
Java: 26
```

## Future Work

### Using GitHub's Pagination
I would use their pagination with the link headers to get more pages if needed.
GitHub's max per_page is 100.

Using Pagination
https://docs.github.com/en/rest/using-the-rest-api/using-pagination-in-the-rest-api?apiVersion=2026-03-10


### Adding more unit tests
I would implement more unit tests for GitHubDataService and GitHubSevice

### Authenticating the requests to GitHub

I remember when I was doing the Kotlin Coroutines and Channels tutorial
that it calls GitHub and allows setting an API Token
https://kotlinlang.org/docs/coroutines-and-channels.html

### Rate-limiting
This would be good for preventing denial-of-service attacks
I've used Bucket4j before.

### More validation of username
I could check for valid characters and length.
I would need more time to check if this documentation is accurate.
https://docs.github.com/en/enterprise-cloud@latest/admin/managing-iam/iam-configuration-reference/username-considerations-for-external-authentication


References:
https://www.baeldung.com/spring-bucket4j


8/12/2026 
12pm - 1pm (1h)
110pm - 210pm (1h)
230pm - 312pm (42m)
320pm - 354pm (34m)

8/13/2026 
925am - 1212m (2h 47m)


