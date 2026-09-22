# Contributing

## Development standards

1. Keep changes scoped and module-specific.
2. Maintain build order compatibility (`SharedProto` before dependent services).
3. Add or update tests for behavioral changes.
4. Avoid committing secrets and local machine-specific config.

## Local validation checklist

```powershell
cd D:\Microservices\SharedProto; mvn clean install
cd D:\Microservices\profile\profile; mvn clean test
cd D:\Microservices\movie\movie; mvn clean test
cd D:\Microservices\gateway\gateway; mvn clean test
cd D:\Microservices\IAMFrontEnd; npm ci; npm run build
```

## Pull request expectations

1. Clear problem statement and solution summary.
2. Risks/trade-offs documented.
3. Evidence of successful local or CI checks.

