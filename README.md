# API Ranking


## Requirements

- High availability
- Real-time player activity and metrics
- Low latency aggregations and joins
- Scalability to millions of players and thousands of concurrent users
- Reliability
- Minimal operational overhead : focus on game development, not infrastructure
- Fraud and anomaly detection

## Prerequisite

1. Docker must be installed
2. The Docker `dynamodb` volume must be created

```bash
$ docker volume create dynamodb
```

3. The Docker `ranking-api-net` network must be created

```bash
$ docker network create ranking-api-net
```

### Content Negotiation

- Add JSON content negotiation to handle incoming requests with JSON MIME types
- Add JSON serialization to handle incoming request data

## Development

### Auto-Relaod

* Rebuild a project automatically using Gradle

```bash
$ gradlew -t build
# To skip running tests when reloading a project
$ gradlew -t build -x test -i
```

## Production

### Build

* Building project and disable auto-reloading:

```bash
$ ./gradlew build -Pdevelopment=false
```
## Resources

- [Scaling Real-time Gaming Leaderboards for Millions of Players](https://www.youtube.com/watch?v=HAb-tWI8oVk)
- [Designing a Realtime Gaming Leaderboard - Horizontally Scalable and Highly Available](https://www.youtube.com/watch?v=UerkzwZ_zSY)

---

# ktor-sample

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

- [Ktor Documentation](https://ktor.io/docs/home.html)
- [Ktor GitHub page](https://github.com/ktorio/ktor)
- The [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). You'll need
  to [request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up) to join.

## Features

Here's a list of features included in this project:

| Name                                               | Description                                                 |
| ----------------------------------------------------|------------------------------------------------------------- |
| [Koin](https://start.ktor.io/p/koin)               | Provides dependency injection                               |
| [Routing](https://start.ktor.io/p/routing-default) | Allows to define structured routes and associated handlers. |

## Building & Running

To build or run the project, use one of the following tasks:

| Task                          | Description                                                          |
| -------------------------------|---------------------------------------------------------------------- |
| `./gradlew test`              | Run the tests                                                        |
| `./gradlew build`             | Build everything                                                     |
| `buildFatJar`                 | Build an executable JAR of the server with all dependencies included |
| `buildImage`                  | Build the docker image to use with the fat JAR                       |
| `publishImageToLocalRegistry` | Publish the docker image locally                                     |
| `run`                         | Run the server                                                       |
| `runDocker`                   | Run using the local docker image                                     |

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

