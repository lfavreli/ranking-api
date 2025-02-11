# 🏆 Tournament Ranking API

A Kotlin-based API for managing players, tournaments, and leaderboards. This project demonstrates my expertise in designing scalable APIs using **Kotlin**, **Ktor**, **Koin**, **DynamoDB**, and **Clean Architecture** principles.

## 🚀 Features

- **Player & Tournament Management**: Create and update players and tournaments.
- **Leaderboard System**: Fetch rankings and retrieve nearby players.
- **Dependency Injection**: Powered by **Koin** for modular architecture.
- **OpenAPI/Swagger Documentation**: Available at `/api/ranking/v1/swagger-ui.html`.
- **Dockerized Environment**: Both the application and **DynamoDB** run in **Docker**.
- **Hot Reload**: Supports **auto-reload** during development.
- **Custom Exception Handling**: Proper error handling with **StatusPage**.
- **Postman Collection**: Available in `/specs` for easy API testing.

---

## 🔧 Prerequisites

Before running the project, ensure you have:

- ✅ [Docker](https://www.docker.com/) installed
- ✅ [Gradle](https://gradle.org/) installed
- ✅ [JDK 11+](https://adoptopenjdk.net/) installed

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
| Service         | Port  |
|----------------|-------|
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
  - Creating players and updating scores works.
  - Retrieving leaderboards is functional.

- 🔗 **Tightly Coupled with DynamoDB**:
  - The current implementation has a strong dependency on **DynamoDB**, which reduces flexibility.
  - Greater use of **Dependency Injection (DI)** is needed to improve modularity.
  - Moving towards **Clean Architecture** will enhance separation of concerns.

- 🚀 **Scalability Considerations**:
  - Depending on the **read/write volume**, a **CQRS architecture** may be beneficial.
  - **DynamoDB** can remain the write store, while **Rockset** can be used for querying large datasets efficiently.

---

## 🚀 API Future Improvements

### ✅ Test Coverage
- 🧪 **Expand unit and integration tests** to improve reliability.
- 🔍 **Automate API response validation** with Postman test collections.

### 🔒 Authentication & Security
- 🔑 **Implement JWT-based authentication** for secured endpoints.
- 🔐 **Integrate OAuth2 for production environments**.

### 📊 Optimization & Performance
- 🔄 **Introduce pagination** for endpoints returning large datasets.
- 🏎 **Implement caching** to improve response times for frequently accessed data.
- 🚦 **Set up rate limiting** to prevent API abuse.

### 📡 Observability & Monitoring
- 📜 **Enable structured logging and request tracing** for better debugging.
- 📊 **Integrate an APM (Application Performance Monitoring) tool** to track performance and detect bottlenecks.

### 💡 Other Enhancements
- 🏥 **Add health-check endpoints** (liveness and readiness probes).
- 🔄 **Set up a CI/CD pipeline** with GitHub Actions for automated testing and deployments.
- 🏆 **Improve leaderboard logic**:
  - Properly handle **tie-breaking scenarios**.
  - Add **sorting options** for rankings.