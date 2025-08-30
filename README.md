# Require4Testing

A simple Test Management System built for the IPWA02-01 case study.

## Features
- Requirement management
- Test case authoring
- Test run creation
- Assignment of test cases to testers
- Tester result submission
- Authentication & Role-based access (JWT)

## Tech Stack
- Backend: Spring Boot (Web, JPA, Security, Flyway)
- Database: PostgreSQL (Docker Compose)
- Frontend: Vue 3 + Vite + Pinia + PrimeVue
- Auth: JWT
- Testing: JUnit + Spring Boot Test

## Run Locally

### Start Database
```bash
docker compose up -d
