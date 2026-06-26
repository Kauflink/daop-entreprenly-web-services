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

## Deployment

The service is deployed to **Google Cloud Run**. A Cloud Build trigger builds the
container from the `Dockerfile` and rolls out a new revision automatically on every
push to `main` (no manual steps required).

In production the app connects to **Cloud SQL (MySQL)** through the Cloud SQL Java
socket factory, so no public IP or host/port is configured. The Cloud Run service
is set up with:

- Environment variables: `SPRING_PROFILES_ACTIVE=prod`, `CLOUD_SQL_CONNECTION_NAME`
  (`project:region:instance`), `DATABASE_NAME`, `DATABASE_USER`, `DATABASE_PASSWORD`,
  `JWT_SECRET`.
- The Cloud SQL instance attached to the service, and the runtime service account
  granted the `roles/cloudsql.client` role.

## License

See [LICENSE](LICENSE).
