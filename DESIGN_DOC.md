# Design Document: Medical Register Application  
**Version 1.0**

---

## 1. System Architecture Overview  

+----------------+ +-----------------+ +---------------+
| XHTML Frontend | HTTPS | Spring Boot | JDBC | H2 In‑Mem |
| (Static UI) | <----> | Backend API | <----> | Database |
+----------------+ +-----------------+ +---------------+
▲ ▲
| REST (JSON) | OAuth2/JWT
| |
+----------------+ +-----------------+
| Auth0 Identity |<-----> | LDAP Directory |
| Provider (IdP) | | (Simulated) |
+----------------+ +-----------------+


1. **Frontend (XHTML + JS)**  
   - Served as static resources by Spring Boot.  
   - Uses Auth0 SPA‑JS SDK to handle login and acquire JWT.  
   - CRUD calls (`fetch()`) to `/api/patients`.

2. **Backend (Spring Boot)**  
   - Exposes REST endpoints under `/api/patients` for Create, Read, Update, Delete.  
   - Secured as an OAuth2 Resource Server: validates JWTs issued by Auth0.  
   - Business logic in `PatientService`; persistence via Spring Data JPA to H2.

3. **Identity & Access**  
   - **Auth0** fronts an LDAP directory (simulated via Database Connection for local).  
   - Auth0 issues JWTs containing scopes: `read:patients`, `write:patients`.  
   - Spring Security maps scopes → `SCOPE_…` authorities and enforces with `@PreAuthorize`.

---

## 2. Assumptions Made  

- **Auth0 as LDAP Proxy**: We assume an enterprise LDAP is connected to Auth0. Locally we use Auth0’s Database Connection to mimic LDAP logins.  
- **Single‑Tenant**: One Auth0 tenant; no multi‑tenant requirement.  
- **In‑Memory DB**: H2 is used for simplicity—no external database setup.  
- **Static Frontend**: XHTML + plain JS; no Node.js/webpack build.

---

## 3. Integration Design  

| Component           | Endpoint           | Protocol/Format   |
|---------------------|--------------------|-------------------|
| XHTML Frontend      | Auth0 Hosted Pages | OAuth2 Redirect   |
|                     | `/api/patients`    | REST over HTTPS   |
| Spring Boot API     | Auth0 JWK URI      | JWT Validation    |
|                     | H2 Database        | JDBC (Spring JPA) |
| GitHub Actions CI   | GHCR               | Docker Push       |

- **Auth0 SPA‑JS** initializes with  
  ```js
  createAuth0Client({
    domain, client_id, audience,
    cacheLocation: "localstorage",
    useRefreshTokens: true
  })
Spring Security SecurityFilterChain permits static assets and login pages, locks down all /api/** behind JWT checks.

4. Automated Testing
Unit Tests

@WebMvcTest(PatientController) + Mockito for PatientService mocking.

Verify 401 (no token), 403 (missing scope), 200 (with scope + data).

Integration Tests

@SpringBootTest + @AutoConfigureMockMvc with a test JwtDecoder stub.

@DataJpaTest for repository layer.

5. CI/CD Pipeline
Build & Test: GitHub Actions runs mvn verify on each push or PR to main.

Docker Build & Publish: Builds an image tagged ghcr.io/<user>/medical-register:<sha> and pushes to GHCR using CR_PAT secret.

(Optional) Deploy: No‑op step “Skipping deploy” until a cluster is configured.

6. Future Considerations
UI Modernization: Migrate to a SPA framework (React/Angular/Vue).

Database Scaling: Move from H2 to PostgreSQL with Flyway migrations.

RBAC: Define Auth0 roles & map to application authorities.

Observability: Centralized logging (ELK) and metrics (Prometheus + Grafana).

Enhanced CI/CD: Canary deployments, automated rollback on failures.

Security Hardening: Enforce HSTS, input sanitization, rate limiting.
