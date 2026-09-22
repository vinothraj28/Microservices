# Documentation Index (Reviewer Path)

This project keeps markdown files in-repo and curates visibility so external reviewers can get high signal quickly.

## Recruiter-first path (read in order)

1. `README.md` (system overview, architecture, quickstart, CI)
2. `docs/README.md` (this curated path)
3. `IAMFrontEnd/README.md` (frontend role and local usage)
4. `profile/profile/PRINCIPAL_ENGINEER_REVIEW.md` (auth architecture depth)

## Supporting evidence (high signal)

- `profile/profile/AUTHENTICATION_TESTS_QUICKSTART.md`
- `profile/profile/src/test/java/com/microservices/profile/README.md`
- `SHOW_SERVICE_API_TESTS.md`

## Internal deep-dive notes (optional for recruiters)

- `SHOW_SERVICE_IMPLEMENTATION.md`
- `SHOW_SERVICE_FILES_INVENTORY.md`
- `OAuth2_Session_Implementation_Summary.md`
- `OAUTH2_REQUEST_TESTING_GUIDE.md`
- `PRINCIPAL_ENGINEER_AGENT.md`
- `.github/ArchitectAgent.md`
- `.github/FrontEndAgent.md`

## Framework-generated docs (not part of portfolio story)

- `profile/profile/HELP.md`
- `gateway/gateway/HELP.md`
- `movie/movie/HELP.md`

## Operations quick runbook

### Health and startup checks

1. Confirm PostgreSQL is reachable.
2. Start services in this order: Profile -> Movie -> Gateway -> IAMFrontEnd.
3. Verify ports: 8081, 8082, 8080, 4200, gRPC 9090/9091.

### Common failures

| Symptom | Likely Cause | Quick Fix |
|---|---|---|
| `shared-proto` dependency not found | SharedProto not installed locally | Run `mvn clean install` in `SharedProto` first |
| gRPC unavailable from gateway | Profile/Movie gRPC ports not up | Check 9090/9091 listeners and app startup logs |
| Auth requests fail with signature errors | JWT secret mismatch | Ensure consistent `jwt.secret` across services |
| DB connection refused | PostgreSQL not running | Start DB and verify expected databases exist |

### Log locations

- Service logs: console output from each Spring Boot process
- Test reports: `target\surefire-reports\` in each backend module
