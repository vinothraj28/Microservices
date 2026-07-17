# AGENTS.md - Microservices AI Coding Guide

## Architecture Overview

This is a Spring Boot 3.5.14 microservices system with three Maven modules:
- **SharedProto**: Protocol Buffers definitions for gRPC (generates Java stubs)
- **Profile Service** (port 8081, gRPC 9090): Core business logic with PostgreSQL + REST + gRPC server
- **Gateway** (port 8080): API entry point using Spring Cloud Gateway + WebFlux, makes gRPC calls to Profile

### Data Flow
```
Client → Gateway (WebFlux REST) → gRPC call → Profile Service (gRPC server)
                              ↓
                        PostgreSQL
```

## Critical Build & Development Workflow

### Build Order
Always build in this order (dependencies flow downstream):
1. `cd SharedProto && mvn clean install` - Generates gRPC stubs to local Maven repo
2. `cd ../profile/profile && mvn clean install` - Needs SharedProto JAR
3. `cd ../gateway/gateway && mvn clean install` - Needs SharedProto JAR

⚠️ **Important**: SharedProto must be installed to local repository before other services can compile. The Profile and Gateway modules depend on `com.microservices:shared-proto:0.0.1-SNAPSHOT`.

### Protocol Buffers Compilation
SharedProto's `pom.xml` includes the protobuf-maven-plugin that automatically:
- Compiles `.proto` files from `src/main/proto/`
- Generates Java stubs → `target/generated-sources/protobuf/`
- Generates gRPC stubs → `target/generated-sources/protobuf/grpc-java/`

When editing `UserProfile.proto`, the gRPC service interface changes. Rebuild SharedProto and reinstall before rebuilding Profile/Gateway.

### Running Services Locally
```powershell
# Terminal 1: PostgreSQL must be running on localhost:5432
# Create database: CREATE DATABASE MicroServices;

# Terminal 2: Profile Service
cd profile/profile
mvn spring-boot:run

# Terminal 3: Gateway
cd gateway/gateway
mvn spring-boot:run

# Gateway available at http://localhost:8080/api/v1/users/
```

## Key Patterns & Conventions

### 1. gRPC Client-Server Pattern
- **Profile Service**: Implements `UserGrpcService` extending `UserServiceGrpc.UserServiceImplBase`
- **Gateway Service**: Uses `@GrpcClient("profile-service")` to inject `UserServiceBlockingStub`
- **Error Handling**: Maps gRPC `StatusRuntimeException` codes (ALREADY_EXISTS, NOT_FOUND, INVALID_ARGUMENT) to custom exceptions
- **Config**: gRPC endpoints in `application.yml` use `grpc.client.profile-service.address` and `grpc.server.port`

### 2. JWT Authentication (Shared Secret)
Both services use **identical JWT secret** (in `application.yml`):
```
jwt:
  secret: AxXLZtEnBsZcWCG6uBC0wCGYCRhrV2D+XVg08SNlk8yt4JQ9Ga/hgaSuqhDlL2qDLA9vDOPqVlNJQyxKBAW87Q==
```
- Profile: `JWTFilter` validates Bearer tokens on incoming REST requests
- Gateway: Routes unprotected `/login` and `/register` endpoints; other paths may be protected
- Token generation: `TokenService.generateToken()` uses HS512 signing

### 3. DTO Mapping with MapStruct
- Use `@Mapper(componentModel = "spring")` for Spring integration
- Profile service uses `UserProfileMapper` to convert between:
  - `UserProfile` (JPA entity) ↔ `UserResponseDTO` (REST/internal)
  - `RegisterRequest` (gRPC proto) → `UserRequestDTO`
  - Implement mapping logic with `default` methods for complex transformations (see `mapRoles()`)
- Add MapStruct processor to `maven-compiler-plugin` annotationProcessorPaths

### 4. Entity Hierarchy
Profile service uses a domain model hierarchy for database entities:
```
BaseEntity (interface with getId())
  ↓
AuditableEntity (@CreationTimestamp, @UpdateTimestamp via Hibernate)
  ↓
UserProfile (@Entity with @Index, @UniqueConstraint on email)
Address (@Entity)
```
Use `GenerationType.UUID` for primary keys.

### 5. SPI Pattern for Extensibility
`PasswordServiceProvider` is a service locator pattern:
- `PasswordServiceProvider.get(algorithmName)` returns `PasswordService` implementation
- Benefits: Swap password hashing algorithms without changing calling code
- Current impl: `ByCryptPasswordService` uses BCrypt

### 6. Data Validation
- **Gateway DTOs**: Use Java records with `jakarta.validation` constraints
  - Example: `RegisterRequestDTO` validates email, password strength, date of birth
  - Controller uses `@Valid` annotation
- **Validation errors**: Caught by exception controller, return structured error responses

### 7. Package Organization
```
com.microservices.profile/
  ├── models/
  │   ├── entities/       (JPA @Entity classes)
  │   ├── audits/         (AuditableEntity base)
  │   ├── enums/          (Roles, PasswordAlgorithm, AddressType)
  │   └── dtos/           (Data transfer objects)
  ├── services/
  │   ├── Impl/           (Implementation classes)
  │   └── interfaces      (Service contracts)
  ├── repositories/       (Spring Data JPA repos)
  ├── mappers/            (MapStruct mappers)
  ├── grpcService/        (gRPC server implementation)
  ├── configurations/     (Spring configs like SecurityConfig)
  ├── controllers/        (REST endpoints)
  ├── filters/            (JWT filter)
  └── exceptions/         (Custom exception classes)
```

## Proto File Patterns

**Location**: `SharedProto/src/main/proto/UserProfile.proto`

Structure:
```protobuf
syntax = "proto3";
package user;
option java_package = "com.microservices.profile.grpc";
option java_multiple_files = true;  // Generate separate files per message/service

service UserService {
  rpc Login (LoginRequest) returns (LoginResponse);
  rpc Register (RegisterRequest) returns (RegisterResponse);
}

message LoginRequest {
  string username = 1;
  string password = 2;
}
```

When adding new RPC methods:
1. Add method to service definition
2. Rebuild SharedProto → regenerates stubs
3. Implement in Profile's `UserGrpcService` extending the base
4. Call from Gateway via injected `UserServiceBlockingStub`

## Common Task Recipes

### Adding a Database Migration
1. Modify entity in `profile/src/main/java/.../models/entities/`
2. Set `spring.jpa.hibernate.ddl-auto: update` (already configured)
3. Restart Profile service—Hibernate will auto-create columns
4. (For production: use Flyway/Liquibase instead)

### Adding a New REST Endpoint to Gateway
1. Create DTO in `gateway/.../DTOS/` with validation rules
2. Add method to `UserController` with `@PostMapping`
3. Call `userGRPCService.methodName()` to delegate to Profile service
4. Handle `StatusRuntimeException` for gRPC errors

### Adding a New gRPC Service
1. Add RPC method to `UserProfile.proto`
2. Run `mvn clean install` in SharedProto
3. Implement method in Profile's `UserGrpcService`
4. Create client call in Gateway's `UserGRPCService`
5. Create REST endpoint in `UserController`

### Debugging gRPC Communication
- gRPC logs available via `io.grpc` logger (Springframework default is INFO)
- Set `logging.level.io.grpc=DEBUG` in `application.yml`
- Profile service gRPC port: 9090 (plaintext, no TLS)
- Gateway configured with `negotiationType: plaintext`

## Testing Endpoints

### Using curl/Postman
```bash
# Register
POST http://localhost:8080/api/v1/users/register
Content-Type: application/json
{
  "userName": "john",
  "emailAddress": "john@example.com",
  "dob": "1990-01-15",
  "password": "Secure123"
}

# Login
POST http://localhost:8080/api/v1/users/login
{
  "username": "john@example.com",
  "password": "Secure123"
}
```

## Dependency Management

### Key Dependencies
- Spring Boot 3.5.14 (Java 21+)
- gRPC 1.64.0 + grpc-spring-boot-starter 3.1.0
- Spring Cloud Gateway (WebFlux)
- Spring Data JPA + PostgreSQL
- JJWT 0.12.5 (JWT)
- MapStruct 1.5.5 (DTO mapping)
- Lombok (annotation processing)
- Protobuf 3.25.3

**No external CVE vulnerabilities expected** - dependencies are current as of June 2026.

## Troubleshooting

| Issue | Diagnosis | Solution |
|-------|-----------|----------|
| gRPC "Address already in use" | Port 9090 conflict | Check `grpc.server.port` in Profile's `application.yml` |
| "Dependency not found: shared-proto" | SharedProto not installed | Run `cd SharedProto && mvn install` first |
| JWT validation fails | Secret mismatch | Ensure both services have identical `jwt.secret` values |
| PostgreSQL connection refused | DB not running | Start PostgreSQL, create database `MicroServices` |
| gRPC method not found | Stubs out of date | Rebuild SharedProto and rebuild dependent services |

## Notes for AI Agents

- **When editing proto files**: Always cascade the build: SharedProto → Profile → Gateway
- **When modifying entities**: Changes auto-apply via Hibernate if `ddl-auto: update`; consider entity relationships (AuditableEntity, BaseEntity)
- **For DTO work**: Use MapStruct `@Mapping` annotations; avoid manual getters/setters
- **For gRPC calls**: Always handle `StatusRuntimeException`; map gRPC status codes to domain exceptions
- **Gateway is reactive (WebFlux)**: Blocking calls from gRPC are acceptable; WebFlux wraps them

