# Blog Platform Backend

A RESTful blog platform backend built with **Java, Spring Boot, PostgreSQL, Spring Data JPA, Spring Security, and JWT**.

The backend provides APIs for user authentication, blog posts, categories, and tags, with support for draft and published posts, relational persistence, request validation, and stateless JWT-based authentication.

## Features

* User authentication with JWT
* Stateless Spring Security configuration
* Create, read, update, and delete blog posts
* Draft and published post states
* Retrieve draft posts for the authenticated user
* Filter published posts by category and tag
* Category management
* Tag management
* Post–category relationships
* Post–tag many-to-many relationships
* DTO-based API responses
* MapStruct entity/DTO mapping
* Request validation
* Centralized error handling
* PostgreSQL persistence
* Docker Compose development environment

## Tech Stack

| Technology      | Purpose                    |
| --------------- | -------------------------- |
| Java 21         | Backend language           |
| Spring Boot     | Application framework      |
| Spring Web      | REST API                   |
| Spring Data JPA | Persistence                |
| Hibernate       | ORM                        |
| Spring Security | Authentication             |
| JWT / JJWT      | Token-based authentication |
| PostgreSQL      | Relational database        |
| MapStruct       | DTO/entity mapping         |
| Lombok          | Boilerplate reduction      |
| Maven           | Build management           |
| Docker Compose  | Local infrastructure       |

## Architecture

The application follows a layered backend architecture:

```text
Client
  │
  ▼
Controllers
  │
  ▼
Services
  │
  ▼
Repositories
  │
  ▼
PostgreSQL
```

Security is applied at the request boundary:

```text
Client
  │
  │ Authorization: Bearer <JWT>
  ▼
Spring Security
  │
  ▼
JWT Authentication Filter
  │
  ▼
Security Context
  │
  ▼
Controller
  │
  ▼
Service
  │
  ▼
Repository
  │
  ▼
PostgreSQL
```

### Project Structure

```text
src/main/java/com/sachin/blog/

├── config/
├── controllers/
├── domain/
│   ├── dtos/
│   └── entities/
├── mappers/
├── repositories/
├── security/
├── services/
└── BlogApplication.java
```

## Domain Model

The core domain consists of:

```text
User
  │
  │ authors
  ▼
Post
 ├── belongs to → Category
 └── has many   → Tag
```

### Post

Posts support two states:

```text
DRAFT
PUBLISHED
```

### User → Post

* A user can author multiple posts.
* Each post has a single author.

### Category → Post

* A post belongs to a category.
* Categories exist independently from posts.

### Post ↔ Tag

* A post can have multiple tags.
* A tag can belong to multiple posts.
* The relationship is represented using a join table.

## REST API

All endpoints are versioned under:

```text
/api/v1
```

### Authentication

| Method | Endpoint             | Description                           |
| ------ | -------------------- | ------------------------------------- |
| `POST` | `/api/v1/auth/login` | Authenticate a user and receive a JWT |

### Posts

| Method   | Endpoint               | Description                                |
| -------- | ---------------------- | ------------------------------------------ |
| `GET`    | `/api/v1/posts`        | Get published posts                        |
| `POST`   | `/api/v1/posts`        | Create a post                              |
| `GET`    | `/api/v1/posts/{id}`   | Get a specific post                        |
| `PUT`    | `/api/v1/posts/{id}`   | Update a post                              |
| `DELETE` | `/api/v1/posts/{id}`   | Delete a post                              |
| `GET`    | `/api/v1/posts/drafts` | Get draft posts for the authenticated user |

### Categories

| Method   | Endpoint                  | Description        |
| -------- | ------------------------- | ------------------ |
| `GET`    | `/api/v1/categories`      | Get all categories |
| `POST`   | `/api/v1/categories`      | Create a category  |
| `DELETE` | `/api/v1/categories/{id}` | Delete a category  |

### Tags

| Method   | Endpoint            | Description  |
| -------- | ------------------- | ------------ |
| `GET`    | `/api/v1/tags`      | Get all tags |
| `POST`   | `/api/v1/tags`      | Create a tag |
| `DELETE` | `/api/v1/tags/{id}` | Delete a tag |

## Authentication

The application uses **Spring Security with JWT-based authentication**.

The authentication flow is:

```text
POST /api/v1/auth/login
        │
        ▼
Authenticate credentials
        │
        ▼
Generate JWT
        │
        ▼
Return token to client
        │
        ▼
Authorization: Bearer <JWT>
        │
        ▼
JWT Authentication Filter
        │
        ▼
Security Context
        │
        ▼
Protected API
```

The application uses stateless authentication rather than server-side sessions.

> **Note:** Resource-level authorization is not currently implemented.

## Persistence

PostgreSQL is used as the relational database.

Spring Data JPA provides repository abstractions, while Hibernate handles ORM and entity persistence.

```text
Service
   │
   ▼
Repository
   │
   ▼
Spring Data JPA
   │
   ▼
Hibernate
   │
   ▼
PostgreSQL
```

Custom repository queries are used for post filtering based on publication status, category, and tags.

## DTO Mapping

The API uses DTOs to separate the HTTP/API representation from persistence entities.

```text
Request
   │
   ▼
 DTO
   │
   ▼
Service
   │
   ▼
Entity
   │
   ▼
Database
```

**MapStruct** is used for entity-to-DTO and DTO-to-entity mapping.

## Running Locally

### Prerequisites

* Java 21+
* Maven
* Docker
* Docker Compose
* Git

### Clone the repository

```bash
git clone https://github.com/s4chinjha/blog_platform_backend.git
cd blog_platform_backend
```

### Start the database

```bash
docker compose up -d
```

### Run the backend

Using the Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using Maven:

```bash
mvn spring-boot:run
```

### Adminer

The local database administration interface is available at:

```text
http://localhost:8888
```

## Project Status

**Backend implementation complete.**

The repository currently contains the core blog platform backend, including REST APIs, JPA relationships, PostgreSQL persistence, DTO mapping, validation, JWT authentication, Spring Security, and Docker-based local development.

Resource-level authorization is not currently implemented.

## Documentation

Additional project documentation is available in [`docs/`](docs/):

* [Learning Notes](docs/LEARNING.md) — implementation notes and concepts explored during development.
* [Future Improvements](docs/FUTURE-IMPROVEMENTS.md) — planned extensions and engineering improvements.
* [Credits](docs/CREDITS.md) — project references and learning resources.
