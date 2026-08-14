# Future Improvements

This document tracks planned improvements for the Blog Platform Backend beyond the current guided implementation.

The goal is to gradually move the project from a completed guided backend implementation toward a more independently engineered and production-oriented application.

## Security & Authorization

* [ ] Implement fine-grained authorization.
* [ ] Add resource ownership checks for posts.
* [ ] Ensure users can modify or delete only resources they are authorized to manage.
* [ ] Review and strengthen endpoint-level security.
* [ ] Evaluate enabling CSRF protection where appropriate.
* [ ] Implement refresh-token support.

## Testing

* [ ] Add unit tests for the service layer.
* [ ] Add tests for authentication and JWT handling.
* [ ] Add controller/API tests.
* [ ] Add integration tests for database operations.
* [ ] Test authorization rules once implemented.
* [ ] Add tests for important error scenarios.

## API & Application Improvements

* [ ] Improve API documentation.
* [ ] Review API response consistency.
* [ ] Improve validation and error responses where required.
* [ ] Review pagination and filtering behavior.
* [ ] Improve the post reading-time calculation.
* [ ] Fix/improve tag and post count update behavior.

## Configuration & Secrets

* [ ] Move environment-specific configuration out of application properties.
* [ ] Remove development secrets from configuration.
* [ ] Introduce environment variables for sensitive configuration.
* [ ] Improve secret management for JWT signing keys and database credentials.

## Production Readiness

* [ ] Add production-specific configuration.
* [ ] Containerize the application.
* [ ] Prepare the application for deployment.
* [ ] Add database migration management.
* [ ] Improve application logging.
* [ ] Add health checks.
* [ ] Introduce application observability and monitoring.

## Code Quality

* [ ] Review service and repository boundaries.
* [ ] Improve naming and consistency across the codebase.
* [ ] Refactor duplicated or unnecessarily complex logic.
* [ ] Review transaction boundaries.
* [ ] Improve documentation for non-obvious implementation decisions.

## Development Workflow

* [ ] Continue using GitHub Issues for individual improvements and bugs.
* [ ] Use focused feature/fix branches.
* [ ] Add pull requests for significant changes.
* [ ] Keep commits focused on a single change.
* [ ] Link commits and pull requests to relevant issues.

## Priority Order

The planned work will be approached in roughly this order:

```text
Authorization
     ↓
Resource Ownership
     ↓
Automated Testing
     ↓
Security Improvements
     ↓
API Improvements
     ↓
Configuration & Secrets
     ↓
Production Readiness
     ↓
Observability
```

The list is intentionally maintained as a living backlog and will be updated as improvements are implemented.
