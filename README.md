# Entreprenly Platform - Web Services

RESTful API backend for the Entreprenly application, built with **Domain-Driven Design (DDD)** and **CQRS**.

## Tech Stack

- **Language:** Java 26
- **Framework:** Spring Boot 4.0.6
- **Database:** MySQL 8+ (Google Cloud SQL in production)
- **Build:** Maven (via the included Maven Wrapper)
- **Security:** Spring Security with JWT (BCrypt password hashing)
- **Documentation:** SpringDoc OpenAPI (Swagger UI)

## Architecture

Bounded contexts under `src/main/java/online/entreprenly/platform`:

| Context | Responsibility |
|---|---|
| `iam` | Identity & Access Management: users, roles, authentication, JWT |
| `inventory` | Products and lots (unit / weight), stock alerts |
| `sales` | Sales, cash registers |
| `subscription` | Plans, billing, payment confirmation |
| `profile` | User profile, preferences, notification settings |
| `chatbot` | WhatsApp sessions, conversations, chat messages and orders |
| `shared` | Domain bases, persistence, OpenAPI, i18n, error handling |

Each context is layered as `domain` / `application` / `infrastructure` / `interfaces`.

## Getting Started

### Prerequisites

- Java 26 JDK
- MySQL 8+ (a local instance for development)
- Docker (optional, for containerized runs)

### Configuration

Copy `.env.example` to `.env` (or export the variables) and adjust the values.
The development profile defaults to a local MySQL instance and creates the
`entreprenly` database automatically.

### Run

```bash
# Development (default profile, local MySQL)
./mvnw spring-boot:run

# Build a runnable jar
./mvnw clean package

# Run with Docker (prod profile)
docker build -t entreprenly-platform .
docker run --env-file .env -p 8092:8092 entreprenly-platform
```

The API is served under `/api/v1` and Swagger UI is available at
`http://localhost:8092/swagger-ui.html`.

## License

See [LICENSE](LICENSE).
