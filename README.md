# Require4Testing

A simple Test Management System built for the IPWA02-01 case study.

## Features
- Requirement management
- Test case authoring
- Test run creation
- Assignment of test cases to testers
- Tester result submission

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
```

### Start Backend
```bash
./mvnw spring-boot:run
```

### Start Frontend
```bash
cd frontend
npm install
npm run dev
```

### Documentation

Architecture Diagram:

Database ERD:

Screenshots: see docs/screenshots/
