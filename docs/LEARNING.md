# Learning Notes

This document records the concepts, implementation decisions, debugging experiences, and backend engineering ideas explored while building the Blog Platform Backend.

The project was developed through a guided implementation and used as practical experience with Spring Boot, JPA, REST APIs, PostgreSQL, Spring Security, JWT authentication, DTOs, and layered backend architecture.

---

## 1. Domain Modeling

The first step was identifying the main nouns in the blogging system.

The core entities are:

```text
User
Post
Category
Tag
```

Posts also have a status:

```text
DRAFT
PUBLISHED
```

This gives the application a simple domain model around users creating and organizing blog posts.

---

## 2. JPA Relationships

A major part of the implementation was understanding how the relationships between the domain entities are represented at the database level.

### User → Post

A user can create multiple posts, while each post has one author.

```text
User 1 ─────────── * Post
```

The relationship uses `@JoinColumn` to specify the column used to establish the relationship.

Cascade behavior and orphan removal were used for the User → Post relationship so that posts are tied to the lifecycle of their author.

### Category → Post

A category can be associated with multiple posts.

The category and post are treated as independent entities, so cascading deletion is not used between them.

Deleting a post should not delete its category.

### Post ↔ Tag

Posts and tags have a many-to-many relationship.

```text
Post * ─────────── * Tag
```

A `Set` is used for the collection so duplicate tag associations are avoided.

A join table is used to map the relationship between post IDs and tag IDs.

Posts and tags are treated as independent entities, so cascade deletion is not used.

These relationship decisions were made while implementing the domain model and persistence layer.

---

## 3. Repository Layer

Repositories were created for the main entities:

* User
* Category
* Tag
* Post

The repositories extend `JpaRepository` using the entity type and its ID type.

For example:

```text
JpaRepository<User, UUID>
```

The repository abstraction provides standard CRUD operations and support for additional repository functionality such as pagination.

The basic repository creation process was:

1. Create the repository package.
2. Create the repository interface.
3. Add the `@Repository` annotation.
4. Extend `JpaRepository<Entity, UUID>`.

---

## 4. REST API Development

After establishing the domain and persistence layers, the application was developed around REST endpoints.

The implementation progressed through:

```text
Categories
    ↓
Tags
    ↓
Posts
    ↓
Draft Posts
    ↓
Create Post
    ↓
Update Post
    ↓
Get Post
    ↓
Delete Post
```

Controllers are responsible for receiving HTTP requests and returning HTTP responses, while services handle the application logic and repositories handle persistence.

---

## 5. DTO Layer

DTOs were introduced to represent data exchanged through the API.

For example, the authentication flow introduced:

```text
LoginRequest
AuthResponse
```

The post implementation introduced:

```text
PostDto
AuthorDto
CreatePostRequest
```

Tags also use a dedicated response DTO.

This keeps API representations separate from persistence entities.

---

## 6. MapStruct

MapStruct was introduced to simplify conversion between entities and DTOs.

The general flow became:

```text
Entity
   │
   ▼
Mapper
   │
   ▼
DTO
   │
   ▼
HTTP Response
```

For example, when retrieving tags:

```text
Tag
 ↓
TagMapper
 ↓
TagResponse
 ↓
HTTP 200
```

This avoided manually constructing response DTOs throughout controller methods.

During the Tag implementation, a mapping mistake occurred because the mapping expected the posts collection to be a `List` while the entity relationship had been implemented using a `Set`.

The issue was identified and corrected before testing the endpoint with Postman and the frontend.

---

## 7. Tag API

The Tag implementation introduced:

* `TagResponse`
* `TagService`
* `TagServiceImpl`
* `TagMapper`
* `TagController`
* `TagRepository`

The list-tags operation retrieves tags through the service layer and maps the entities to response DTOs.

A custom repository query was also introduced to retrieve tags together with their post count:

```text
@Query("SELECT t FROM Tag t LEFT JOIN t.posts")
List<Tag> findAllWithPostCount();
```

The result is then mapped into the API response.

---

## 8. Post Filtering

The post listing functionality introduced filtering based on:

* Category
* Tag
* Category + Tag
* Published status

The implementation required retrieving the category and tag entities when their IDs were provided.

The category service was extended with a `getById` operation that returns the entity or raises an entity-not-found error.

Repository queries were then introduced to retrieve posts using the relevant status, tag ID, and category ID.

The controller receives the result from the service layer and converts the posts into `PostDto` objects through `PostMapper`.

---

## 9. JWT Authentication

The application was then secured using JWT-based authentication.

The authentication flow consists of:

```text
Login Request
     ↓
Authentication Service
     ↓
Authenticate credentials
     ↓
Load UserDetails
     ↓
Generate JWT
     ↓
Return AuthResponse
```

The authentication service uses:

* `AuthenticationManager`
* `UserDetailsService`
* JWT secret key
* JWT expiration time

The authentication process uses:

```java
UsernamePasswordAuthenticationToken
```

to authenticate the supplied email and password.

After successful authentication, the user's `UserDetails` are used to generate the JWT.

---

## 10. JWT Generation

The generated JWT contains claims including:

* Subject
* Issued-at timestamp
* Expiration timestamp

The token is signed using an HMAC SHA-256 signing algorithm.

Conceptually:

```text
UserDetails
    ↓
Claims
    ↓
Subject
    ↓
Issued At
    ↓
Expiration
    ↓
Signing Key
    ↓
JWT
```

The JWT secret is configured through application properties.

The signing key is derived from the configured secret.

---

## 11. JWT Authentication Filter

A `JwtAuthenticationFilter` was introduced by extending:

```text
OncePerRequestFilter
```

The purpose is to process the JWT once for each request.

The filter extracts the token from the request and validates it before establishing the authenticated user in the security context.

The filter is then integrated into the Spring Security configuration.

---

## 12. Spring Security

Spring Security was configured to protect authenticated endpoints while allowing the appropriate public endpoints.

The application uses stateless authentication with JWT rather than server-side sessions.

The resulting request flow is:

```text
HTTP Request
     ↓
Spring Security
     ↓
JWT Authentication Filter
     ↓
Validate Token
     ↓
Authenticate User
     ↓
Security Context
     ↓
Controller
```

At this stage, the project implements **authentication**, not full resource-level authorization.

---

## 13. Draft Posts

A dedicated endpoint was introduced for retrieving draft posts belonging to the authenticated user.

The implementation required:

* A new controller method
* User ID from the authenticated request
* `UserService`
* A repository query using the author and post status
* Draft status filtering

The repository operation follows the concept:

```text
findAllByAuthorAndStatus(user, DRAFT)
```

The flow is:

```text
Authenticated Request
       ↓
User ID
       ↓
UserService
       ↓
User
       ↓
PostService
       ↓
Draft Posts
       ↓
PostRepository
```

---

## 14. Create, Update, Get and Delete Posts

The post management functionality was then expanded to cover:

* Creating posts
* Updating posts
* Retrieving individual posts
* Deleting posts

The create operation introduced a dedicated request DTO.

The update and delete implementations required repository queries appropriate to the post being modified.

During the implementation, an important security limitation became apparent: the current implementation does not perform resource ownership checks before modifying a post.

The guided implementation intentionally leaves this for the authorization stage.

---

## 15. Authentication vs Authorization

One of the important distinctions learned during the project was the difference between authentication and authorization.

### Authentication

Answers:

> Who is the user?

The current application handles this through:

```text
Email + Password
       ↓
AuthenticationManager
       ↓
JWT
       ↓
JWT Authentication Filter
```

### Authorization

Answers:

> What is this authenticated user allowed to do?

For example:

```text
User A
  ↓
Owns Post A
  ↓
Can modify Post A

User A
  ↓
Attempts to modify Post B
  ↓
Should be rejected
```

Resource-level authorization has **not yet been implemented** in this project.

This is intentionally identified as a future extension rather than being represented as completed functionality.

---

## 16. Debugging & Development Workflow

The project also provided practical experience debugging problems across multiple backend layers.

A useful workflow developed during implementation was:

```text
Request
  ↓
Security
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

When an endpoint fails, the goal is to determine which layer rejected or incorrectly processed the request instead of immediately changing unrelated code.

GitHub Issues and focused commits were also used to track individual problems.

One example involved tracing the authenticated user ID between the JWT authentication filter and the controller while implementing the draft-post functionality.

---

## 17. Testing the APIs

The endpoints were tested using:

* Postman
* The frontend application

The Tag API was tested through both approaches after implementing the mapping and service layers.

This provided a way to verify the complete request path:

```text
Client
  ↓
HTTP Request
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
  ↓
Response
```

---

## 18. Docker & PostgreSQL

Docker Compose was used to provide the local database environment.

The basic development setup became:

```text
Spring Boot Application
        │
        ▼
    PostgreSQL
        │
        ▼
   Docker Container
```

This allowed the backend to use a reproducible local PostgreSQL environment rather than requiring the database to be installed directly on the development machine.

---

## 19. Transaction Management

Transaction boundaries were introduced at the service layer.

For operations that only read persistent data:

```java
@Transactional(readOnly = true)
```

can be used to express that the transaction is intended for read operations.

The important concept learned here was that transaction management belongs around application/database operations rather than being treated as something that only exists inside repository methods.

---

## 20. Key Implementation Concepts

The project brought together several Spring and backend concepts that had previously been studied individually.

```text
Spring Boot
    +
REST APIs
    +
JPA / Hibernate
    +
PostgreSQL
    +
DTOs
    +
MapStruct
    +
Spring Security
    +
JWT
    +
Docker
    ↓
Complete Backend Application
```

The main progression was from understanding individual framework features to understanding how those features interact across a complete application.

---

## 21. Current Learning State

The guided backend implementation is complete.

The project currently demonstrates practical experience with:

* Domain modeling
* JPA relationships
* Repository design
* REST API development
* Service-layer architecture
* DTOs
* MapStruct
* PostgreSQL
* Hibernate
* JWT authentication
* Spring Security
* Validation
* Error handling
* Transactions
* Docker Compose
* API testing
* Cross-layer debugging
* GitHub-based development

The next stage is to extend the system independently rather than simply continuing the guided implementation.

---

## 22. Further Work

The next development stage is documented separately in:

[`FUTURE-IMPROVEMENTS.md`](FUTURE-IMPROVEMENTS.md)

The planned work includes authorization, resource ownership, automated testing, security improvements, and other production-oriented enhancements.