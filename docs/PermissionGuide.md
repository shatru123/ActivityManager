# Permission Guide

## Phase 1 Permissions

### Internet

- Used for auth API communication
- Requested implicitly by Android through manifest declaration

### Usage Access

- Used to read app foreground/background usage metadata
- Not granted through a standard runtime dialog
- The user must manually approve it from Android settings

What is collected in Phase 1:

- App name
- Package name
- Start time
- End time
- Duration

What is not collected in Phase 1:

- Screen content
- Keystrokes
- Notification message bodies
- Call audio

## Consent UX

The timeline screen explains why Usage Access is needed before any local ingestion is attempted. If the permission is absent, the app shows a dedicated consent card and deep-links into the correct system settings screen.
