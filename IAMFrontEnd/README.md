# IAMFrontEnd

Angular 19 frontend for authentication and user-facing flows in the microservices platform.

## Local development

```bash
npm ci
npm start
```

App URL: `http://localhost:4200`

The app loads `public/app-config.json` on localhost and `public/app-config.production.json` on non-localhost hosts.

## Build and test

```bash
npm run build
npm test
```

## Backend dependency

This frontend is intended to work with the gateway service at `http://localhost:8080`.

For full-system setup, see the repository root `README.md`.
