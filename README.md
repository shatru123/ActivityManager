# LifeLogger AI

LifeLogger AI is a production-oriented personal life tracking platform with:

- A modular Android app built with Kotlin, Jetpack Compose, Hilt, Room, WorkManager, Retrofit, and clean architecture boundaries.
- A .NET backend built with ASP.NET Core 8 Minimal APIs, EF Core, PostgreSQL, JWT auth, Swagger, and Docker.

This repository is being delivered incrementally.

## Phase Status

Phase 1 is implemented in this repository:

- Android project setup
- Secure register/login flow
- Local encrypted session storage
- Room database foundation
- UsageStatsManager-based app usage ingestion
- Daily timeline UI
- Periodic local usage refresh with WorkManager
- ASP.NET Core auth backend with JWT and refresh tokens

Planned for Phase 2:

- Activity upload and reporting APIs
- Secure sync engine
- Dashboard/reporting features

Planned for Phase 3:

- Location tracking
- Activity recognition
- AI summaries
- Analytics and search

## Repository Layout

- [`android-app`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/android-app): Android client
- [`dotnet-app`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/dotnet-app): ASP.NET Core backend
- [`docs/Architecture.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/Architecture.md)
- [`docs/DatabaseSchema.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/DatabaseSchema.md)
- [`docs/ApiDocumentation.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/ApiDocumentation.md)
- [`docs/InstallationGuide.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/InstallationGuide.md)
- [`docs/PermissionGuide.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/PermissionGuide.md)
- [`docs/DeploymentGuide.md`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/docs/DeploymentGuide.md)

## Build Verification

Verified in this workspace:

- `dotnet build dotnet-app/LifeLogger.sln`
- `cd android-app && ./gradlew :app:assembleDebug`

## Quick Start

Backend:

1. `cd dotnet-app`
2. `docker compose up -d postgres`
3. `dotnet run --project src/LifeLogger.Api`
4. Open Swagger at `https://localhost:7xxx/swagger` or the URL shown by ASP.NET Core

Android:

1. Open [`android-app`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/android-app) in Android Studio
2. Start an emulator
3. Ensure the backend is reachable at `http://10.0.2.2:8080/` for debug builds
4. Run the `app` module
5. Register, then grant Usage Access when prompted from the timeline screen

## Security Notes

- JWT refresh tokens are issued by the backend and stored hashed server-side.
- The Android client stores session secrets in `EncryptedSharedPreferences`.
- Consent is explicit for Usage Access before local activity ingestion occurs.
- Notification content and call content are not captured in Phase 1.
