# Database Schema

## Android Room

### `activity_logs`

- `id` `INTEGER PRIMARY KEY`
- `appName` `TEXT NOT NULL`
- `packageName` `TEXT NOT NULL`
- `startTimeEpochMillis` `INTEGER NOT NULL`
- `endTimeEpochMillis` `INTEGER NOT NULL`
- `durationMillis` `INTEGER NOT NULL`

### `location_logs`

- `id`
- `latitude`
- `longitude`
- `speedMetersPerSecond`
- `accuracyMeters`
- `address`
- `capturedAtEpochMillis`

### `notification_logs`

- `id`
- `appName`
- `title`
- `timestampEpochMillis`

### `device_logs`

- `id`
- `batteryPercentage`
- `chargingState`
- `wifiEnabled`
- `bluetoothEnabled`
- `networkType`
- `recordedAtEpochMillis`

### `daily_summaries`

- `dateIso` `PRIMARY KEY`
- `summaryText`
- `topAppName`
- `topAppDurationMillis`
- `generatedAtEpochMillis`

### `sync_queue`

- `id`
- `entityType`
- `entityId`
- `payloadJson`
- `status`
- `retryCount`
- `nextAttemptAtEpochMillis`

## Backend PostgreSQL

### `users`

- `id` `uuid primary key`
- `full_name` `varchar(120) not null`
- `email` `varchar(256) not null unique`
- `password_hash` `varchar(512) not null`
- `created_at_utc`
- `updated_at_utc`

### `refresh_tokens`

- `id` `uuid primary key`
- `user_id` `uuid not null`
- `token_hash` `varchar(256) not null unique`
- `expires_at_utc`
- `revoked_at_utc`
- `created_by_ip`
- `user_agent`
- `replaced_by_token_hash`
- `created_at_utc`
- `updated_at_utc`

## Planned Phase 2 Backend Tables

- `activity_upload_batches`
- `activity_logs`
- `location_logs`
- `device_logs`
- `daily_reports`
