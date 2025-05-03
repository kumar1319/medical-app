
# Medical Register

**A simple Spring Boot + XHTML application** for managing patient records, secured with Auth0 (OAuth2 + JWT).

## Table of Contents

- [Features](#features)  
- [Prerequisites](#prerequisites)  
- [Getting Started](#getting-started)  
- [Running Locally](#running-locally)  
- [Docker](#docker)  
- [CI/CD](#cicd)  
- [Testing](#testing)  
- [Design Document](#design-document)  
- [Future Enhancements](#future-enhancements)  

---

## Features

- CRUD REST API for Patients (`name`, `age`, `medicalHistory`)  
- Secured by Auth0 (simulated LDAP) with OAuth2 Resource Server  
- Static XHTML front‑end for login + data entry/viewing  
- In‑memory H2 database for easy demo  
- GitHub Actions CI pipeline (build → test → Docker image push)  

---

## Prerequisites

- Java 17 + Maven  
- (Optional) Docker & Docker Desktop  
- An Auth0 tenant with a “Database” connection and an API named `medical-api`  
- GitHub Actions secret `CR_PAT` (for GHCR)  

---

## Getting Started

1. **Clone the repo**  
   ```bash
   git clone https://github.com/kumar1319/medical-register.git
   cd medical-register

- Configure Auth0

  - Create an Auth0 Application (Single‑Page App) and an API (medical-api).

  - In src/main/resources/application.yml, set:
 
        spring:
      security:
        oauth2:
          resourceserver:
            jwt:
              issuer-uri: [https://YOUR_DOMAIN/](https://dev-sqo40jqvj08mzb4x.us.auth0.com/)
    auth0:
      issuer: [https://YOUR_DOMAIN/](https://dev-sqo40jqvj08mzb4x.us.auth0.com/)
      audience: [YOUR_API_IDENTIFIER](https://medical-api)

- Under “Allowed Callback URLs” in Auth0, add http://localhost:8080/patients.xhtml.

** Running Locally **

mvn clean spring-boot:run

- Visit http://localhost:8080/index.xhtml

- You’ll be redirected to Auth0’s login. Use a test user from your Auth0 Database Connection.

- After login, you can add, view, and delete patients.

**Docker**

Build and run the container locally:

# Build the JAR
mvn clean package -DskipTests

# Build the Docker image
docker build -t medical-register .

# Run it
docker run --rm -p 8080:8080 \
  -e SPRING_SECURITY_OAUTH2_RESOURCESERVER_JWT_ISSUER-URI=https://YOUR_DOMAIN/ \
  -e AUTH0_ISSUER=https://YOUR_DOMAIN/ \
  -e AUTH0_AUDIENCE=YOUR_API_IDENTIFIER \
  medical-register
  
**CI/CD
We use GitHub Actions to:**

- Build & test on every push/PR to main

- Build a Docker image and push to GitHub Container Registry (GHCR)

- (Optional) Deploy to Kubernetes—currently a no‑op until a cluster is configured

See .github/workflows/ci.yml for details.

**Testing**
Unit Tests:

mvn test
**Integration Tests:**

mvn verify

**Design Document**
See DESIGN_DOC.md for the 2‑page architecture, integration design, assumptions, and future considerations.

**Future Enhancements**
- Upgrade to a modern SPA (React/Vue/Angular)

- Switch H2 → PostgreSQL with Flyway migrations

- Add Role‑Based Access Control (RBAC)

- Integrate centralized logging & metrics

- Implement canary deployments in CI/CD





  
