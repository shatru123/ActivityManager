# Installation Guide

## Prerequisites

### Android

- Android Studio Iguana or newer
- JDK 17
- Android SDK Platform 35
- Android Build Tools 35

### Backend

- .NET SDK 8
- Docker Desktop

## Backend Setup

1. Open a terminal in [`dotnet-app`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/dotnet-app)
2. Start PostgreSQL:

```bash
docker compose up -d postgres
```

3. Run the API:

```bash
dotnet run --project src/LifeLogger.Api
```

4. Verify:

```bash
dotnet build LifeLogger.sln
```

## Android Setup

1. Open [`android-app`](/Users/shatrughnaambhore/Shatru/Learning/Projects/ActivityManager/ActivityManager/android-app) in Android Studio
2. Let Gradle sync
3. Start an Android emulator
4. Ensure the backend is reachable from the emulator at `http://10.0.2.2:8080/`
5. Run the `app` configuration

## Build Commands

Backend:

```bash
dotnet build dotnet-app/LifeLogger.sln
```

Android:

```bash
cd android-app
./gradlew :app:assembleDebug
```
