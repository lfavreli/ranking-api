# 🏆 Ranking API

A Kotlin-based API for managing players, tournaments, and leaderboards.

## 🚀 Features

- **Player & Tournament Management**: Create and update players and tournaments.
- **Leaderboard System**: Fetch rankings and retrieve nearby players.
- **Modular Architecture**: Powered by **Koin** for efficient Dependency Injection (DI).
- **API Documentation**: OpenAPI/Swagger UI available at `/api/ranking/v1/swagger-ui.html`.
- **Dockerized Setup**: Both the application and **DynamoDB** run in **Docker**.
- **Hot Reload**: Supports **auto-reload** for development.
- **Error Handling**: Custom exception handling via **StatusPage**.
- **Postman Collection**: Available in `/specs` for easy API testing.
- **Automated DynamoDB Setup**: A bash script in the `/scripts` folder to initialize and reset the database.

---

## 🔧 Prerequisites

Before running the project, ensure you have:

- ✅ [Docker](https://www.docker.com/) installed 
  - ✅ **Docker Network**: `ranking-api-net` (Required for `docker compose` to work)
  - ✅ **Docker Volume**: `dynamodb` (Used for local DynamoDB persistence)
- ✅ [Gradle](https://gradle.org/) installed
- ✅ [JDK 11+](https://adoptopenjdk.net/) installed

### 🔄 Setup Required Network & Volume

If the network and volume do not exist, create them using:

```sh
docker network create ranking-api-net
docker volume create dynamodb
```

This ensures that users have everything they need to run the application smoothly.

---

## 🛠 How to Run

### 🏗 Development Mode
Run the following commands:

```sh
docker compose up        # Starts DynamoDB and DynamoDB Admin services
./gradlew run            # Starts the Ktor application
```

### 🐳 Containerized Mode
Run the project as fully Dockerized:

```sh
docker compose --profile full up
```

### 📡 Service Ports
| Service        | Port   |
|----------------|--------|
| Application    | `8080` |
| DynamoDB       | `8000` |
| DynamoDB Admin | `8001` |

---

## 🎯 API Endpoints

📝 For full API documentation, visit the Swagger UI at:

```sh
http://localhost:8080/api/ranking/v1/swagger-ui.html
```

---

## ⚠️ Known Limitations

- 🚧 **Partial API Implementation**:
  - Creating players, tournaments and updating scores works.
  - Deleting tournament players works.
  - Retrieving leaderboards, players and tournaments is functional.
  - Other _endpoints_ have to be implemented.

- 🔗 **Tightly Coupled with DynamoDB**:
  - The current implementation has a strong dependency on **DynamoDB**, which reduces flexibility.
  - Greater use of **Dependency Injection (DI)** is needed to improve modularity.
  - Moving towards **Clean Architecture** will enhance separation of concerns.

- 🚀 **Scalability Considerations**:
  - Depending on the **read/write volume**, a **CQRS architecture** may be beneficial.
  - **DynamoDB** can remain the write store, while **Rockset** can be used for querying large datasets efficiently.

---

## 🚀 Future Improvements

### ✅ Test Coverage
- **Expand unit and integration tests** to improve reliability.
- **Automate API response validation** with Postman test collections.

### 🔒 Authentication & Security
- **Integrate Basic Authentication** for Setup & Destroy database endpoints.
- **Implement JWT-based authentication** for secured endpoints.

### 📊 Optimization & Performance
- **Introduce pagination** for endpoints returning large datasets.
- **Implement caching** to improve response times for frequently accessed data.
- **Set up rate limiting** to prevent API abuse.

### 📡 Observability & Monitoring
- **Enable structured logging and request tracing** for better debugging.
- **Integrate an Application Performance Monitoring (APM) tool** to track performance and detect bottlenecks.

### 💡 Other Enhancements
- **Add health-check endpoints** (liveness and readiness probes).
- **Set up a CI/CD pipeline** with GitHub Actions for automated testing and deployments.
- **Improve leaderboard logic**:
  - Properly handle **tie-breaking scenarios**.
  - Add **sorting options** for rankings.

---

## 📬 Contact

📧 Loïc FAVRELIERE - [LinkedIn](https://www.linkedin.com/in/loic-favreliere/) <br/>
🐙 GitHub - [@lfavreli](https://github.com/lfavreli)
