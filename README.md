# Microservices Platform (Gateway + gRPC Services + Angular Frontend)

This repository demonstrates a Java 21 microservices platform with a Spring Cloud Gateway entrypoint, gRPC service-to-service communication, PostgreSQL persistence, JWT-based auth flows (including MFA), and an Angular frontend.

## Architecture at a glance

```text
IAMFrontEnd (Angular, :4200)
        |
        v
Gateway Service (WebFlux REST, :8080)
   |                     |
   | gRPC (:9090)        | gRPC (:9091)
   v                     v
Profile Service      Movie Service
(:8081, gRPC :9090)  (:8082, gRPC :9091)
        |                   |
        +------ PostgreSQL -+
```

## Repository modules

| Module | Purpose | Tech |
|---|---|---|
| `SharedProto` | Shared Protocol Buffers + generated gRPC contracts | Protobuf, gRPC |
| `profile/profile` | User profile, auth, MFA, JWT, REST + gRPC server | Spring Boot, JPA, PostgreSQL |
| `gateway/gateway` | API gateway and gRPC client orchestration | Spring Cloud Gateway, WebFlux, gRPC |
| `movie/movie` | Movie-domain service (REST + gRPC) | Spring Boot, JPA, PostgreSQL |
| `IAMFrontEnd` | Frontend experience for auth/user flows | Angular 19 |

## Senior engineering highlights

1. **Clear service boundaries** via shared contracts (`SharedProto`) and explicit transport layers (REST at edge, gRPC internally).
2. **Security-oriented design** with JWT claims handling and MFA challenge/verification flow.
3. **Extensibility patterns** including mapper-driven DTO transforms and service abstractions.
4. **Tested authentication layer** with dedicated unit test suites in profile service.

## Quickstart (local)

### Prerequisites

- JDK 21+
- Maven 3.9+
- Node.js 20+ and npm
- PostgreSQL 15+

### Build order (important)

```powershell
cd D:\Microservices\SharedProto
mvn clean install

cd D:\Microservices\profile\profile
mvn clean install

cd D:\Microservices\movie\movie
mvn clean install

cd D:\Microservices\gateway\gateway
mvn clean install
```

### Run services

```powershell
# Terminal 1
cd D:\Microservices\profile\profile
mvn spring-boot:run

# Terminal 2
cd D:\Microservices\movie\movie
mvn spring-boot:run

# Terminal 3
cd D:\Microservices\gateway\gateway
mvn spring-boot:run

# Terminal 4
cd D:\Microservices\IAMFrontEnd
npm ci
npm start
```

Endpoints:
- Gateway API: `http://localhost:8080`
- Profile service: `http://localhost:8081`
- Movie service: `http://localhost:8082`
- Frontend: `http://localhost:4200`

## Reproducible environment with Docker Compose

This repo includes:
- `docker-compose.yml`
- Container build files for gateway/profile/movie/frontend
- PostgreSQL bootstrap for required databases

```powershell
cd D:\Microservices
copy .env.example .env
docker compose up --build
```

Local endpoints after startup:
- Gateway API: `http://localhost:8080`
- Profile API: `http://localhost:8081`
- Movie API: `http://localhost:8082`
- Frontend: `http://localhost:4200`

Published service ports:
- PostgreSQL: `5432`
- Profile gRPC: `9090`
- Movie gRPC: `9091`

To stop and remove containers:

```powershell
cd D:\Microservices
docker compose down
```

## Configuration and secrets

Use `.env.example` as the template for environment-driven configuration during demos and local runs:

```powershell
copy .env.example .env
```

Frontend runtime config is host-aware:
- localhost uses `IAMFrontEnd/public/app-config.json`
- Azure Static Web Apps uses `IAMFrontEnd/public/app-config.production.json`

In Azure Container Apps, set the gateway `GRPC_CLIENT_*` env vars directly to the internal service hostnames and ports, and keep `JWT_SECRET`, datasource values, and `SECURITY_ENCRYPTION_KEY` in app settings or Key Vault.

## Quality checks

```powershell
# Backend tests
cd D:\Microservices\profile\profile
mvn test

cd D:\Microservices\gateway\gateway
mvn test

cd D:\Microservices\movie\movie
mvn test

# Frontend build
cd D:\Microservices\IAMFrontEnd
npm run build
```

## API sample

```bash
POST http://localhost:8080/api/v1/users/register
Content-Type: application/json

{
  "userName": "john",
  "emailAddress": "john@example.com",
  "dob": "1990-01-15",
  "password": "Secure123"
}
```

## Documentation index

See `docs/README.md` for a curated reviewer path and links to implementation deep dives.
