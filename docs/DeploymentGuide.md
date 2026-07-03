# Deployment Guide

## Backend Deployment

The backend includes a Dockerfile and `docker-compose.yml`.

### Local Container Flow

```bash
cd dotnet-app
docker compose up --build
```

### Required Production Configuration

- `ConnectionStrings__Database`
- `Jwt__Issuer`
- `Jwt__Audience`
- `Jwt__SigningKey`
- `Jwt__AccessTokenLifetimeMinutes`
- `Jwt__RefreshTokenLifetimeDays`

## Production Recommendations

- Terminate TLS at a reverse proxy or managed ingress
- Store secrets in a managed secret store
- Rotate JWT signing keys carefully
- Replace development database credentials
- Add database migrations to CI/CD
- Add observability for auth and sync failures

## Android Release Preparation

Before shipping Phase 1:

- Replace the debug API base URL
- Point the app to HTTPS only
- Add release signing configuration
- Add privacy policy and explicit consent copy
- Validate Usage Access flows on physical devices from multiple OEMs
