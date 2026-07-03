# API Documentation

## Base

- Local development API project: [`dotnet-app/src/LifeLogger.Api`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/dotnet-app/src/LifeLogger.Api)
- Swagger is enabled in development.

## Implemented in Phase 1

### `POST /api/auth/register`

Request:

```json
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "StrongPassword123!"
}
```

Response:

```json
{
  "userId": "uuid",
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "accessToken": "jwt",
  "refreshToken": "opaque-token",
  "accessTokenExpiresAtUtc": "2026-07-03T12:00:00Z"
}
```

### `POST /api/auth/login`

Request:

```json
{
  "email": "jane@example.com",
  "password": "StrongPassword123!"
}
```

Response: same shape as register.

### `POST /api/auth/refresh`

Request:

```json
{
  "refreshToken": "opaque-token"
}
```

Response: same shape as login.

### `GET /health`

Returns service health metadata.

## Planned Phase 2 Endpoints

These are part of the requested roadmap and intentionally not implemented yet:

- `POST /api/activity/upload`
- `GET /api/activity/day`
- `GET /api/activity/week`
- `GET /api/activity/month`
- `GET /api/report/daily`
- `GET /api/report/search`

## Error Handling

- `400`: invalid request payload
- `401`: invalid credentials or expired refresh token
- `409`: duplicate registration email

## Auth Model

- Access token: signed JWT
- Refresh token: opaque random token, hashed before persistence
