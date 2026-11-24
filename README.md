# Spring Boot CRUD Demo

Production-style Spring Boot CRUD API demonstrating **OOP, SOLID principles, design patterns, unit tests, and QA** with **PostgreSQL**, **RabbitMQ**, and **Redis** via Docker.

[![Java 8](https://img.shields.io/badge/Java-8-blue?logo=java)](https://www.oracle.com/java/)
[![Spring Boot 2.7](https://img.shields.io/badge/Spring%20Boot-2.7.18-green?logo=spring)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue?logo=postgresql)](https://www.postgresql.org/)
[![Redis](https://img.shields.io/badge/Redis-7-red?logo=redis)](https://redis.io/)
[![RabbitMQ](https://img.shields.io/badge/RabbitMQ-3-orange?logo=rabbitmq)](https://www.rabbitmq.com/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## Tech Stack

- Java 8, Spring Boot 2.7.18
- Spring Data JPA + PostgreSQL
- Spring Data Redis (cache-aside)
- Spring AMQP + RabbitMQ (event-driven)
- MapStruct (DTO ↔ Entity mapping)
- Lombok (boilerplate reduction)
- JUnit 5 + Mockito + AssertJ (unit tests)
- Docker Compose (infrastructure)

## Architecture & Design Patterns

| Layer | Responsibility | Pattern |
|-------|---------------|---------|
| `controller` | HTTP request/response handling | Controller |
| `service` (`interface` + `impl`) | Business logic, transaction orchestration | Service Layer |
| `repository` | Data access abstraction | Repository |
| `mapper` | DTO ↔ Entity conversion | Mapper |
| `dto` | Data transfer | DTO |
| `event` + `publisher` | Decoupled async notifications | Publisher / Event-Driven |
| `cache` (Redis) | Read-side caching | Cache-Aside |
| `exception` handler | Centralized error handling | Exception Handler |

## SOLID Applied

- **SRP** – Each class has one reason to change.
- **OCP** – `ProductService` interface; new implementations without modifying clients.
- **LSP** – `ProductServiceImpl` fully substitutable for `ProductService`.
- **ISP** – Focused interfaces; clients depend only on methods they use.
- **DIP** – High-level modules depend on abstractions, injected via constructor.

## Quick Start with Docker

```bash
# 1. Start infrastructure (Postgres, RabbitMQ, Redis)
docker compose up -d

# 2. Verify services are healthy
docker compose ps
```

## Build & Run

Requires **Java 8+** and **Maven 3.8+**.

```bash
# Build
mvn clean package -DskipTests

# Run the application
mvn spring-boot:run
```

The API is available at `http://localhost:8080/api/products`.

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/api/products` | Create a product |
| `GET` | `/api/products/{id}` | Get product by id (cached) |
| `GET` | `/api/products` | List all products (cached) |
| `PUT` | `/api/products/{id}` | Update a product |
| `DELETE` | `/api/products/{id}` | Delete a product |

### Example Request

```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "Gaming laptop",
  "price": 1200.00,
  "stock": 10
}
```

### Example Response

```json
{
  "success": true,
  "message": "Product created successfully",
  "data": {
    "id": 1,
    "name": "Laptop",
    "description": "Gaming laptop",
    "price": 1200.0,
    "stock": 10,
    "createdAt": "2026-11-25T00:00:00Z",
    "updatedAt": "2026-11-25T00:00:00Z"
  },
  "timestamp": "2026-11-25T00:00:00Z"
}
```

## Run Tests

```bash
# Unit tests (Mockito, no Docker required)
mvn test
```

## QA Checklist

- [x] Java 8 source/target compatibility (Spring Boot 2.7)
- [x] Constructor-based dependency injection (no field injection)
- [x] Validation via Bean Validation (`@Valid`, `@NotBlank`, `@DecimalMin`, etc.)
- [x] Centralized exception handling (`@RestControllerAdvice`)
- [x] JPA auditing timestamps (`createdAt`, `updatedAt`)
- [x] Redis cache-aside with eviction on write operations
- [x] RabbitMQ events on create/update/delete
- [x] Unit tests for service, controller, and repository layers
- [x] Docker Compose for local infrastructure

## Project Structure

```
src/main/java/com/demo/crud
├── CrudDemoApplication.java
├── config/              # Redis, RabbitMQ configuration
├── controller/          # REST controllers
├── dto/                 # Request/response classes (ProductRequest, ProductResponse, ApiResponse)
├── entity/              # JPA entities (Product)
├── exception/           # Custom exceptions + global handler
├── mapper/              # MapStruct mappers
├── repository/          # Spring Data JPA repositories
├── service/             # Service interface + implementation
└── event/               # Domain events + publisher
src/test/java/com/demo/crud
├── controller/          # Controller tests (MockMvc)
├── repository/          # Repository tests (DataJpaTest)
└── service/impl/        # Service unit tests (Mockito)
```

## License

MIT License. See [LICENSE](LICENSE) for details.
