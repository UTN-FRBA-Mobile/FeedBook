# Quickstart: Backend Rest Migration

## 1. Start the backend

From the repository root:

```bash
cd back
go test ./...
go run .
```

Expected result:

- The backend listens on `http://127.0.0.1:8080` by default.
- Existing login continues to respond.
- New content endpoints are available under the documented API contract.
- Set `FEEDBOOK_ADDR=0.0.0.0:8080` if you want to bind to all interfaces.

## 2. Point the Android app to the backend

Update `BuildConfig.BACKEND_ORIGIN` in `app/build.gradle.kts` when you want a
different backend environment.

Recommended local values:

1. Android emulator: `http://10.0.2.2:8080/`
2. Physical device with `adb reverse tcp:8080 tcp:8080`: `http://localhost:8080/`
3. Same host desktop tooling: `http://localhost:8080/`

## 3. Verify migrated content flows

Run the Android app and validate:

1. Books list and book detail load backend-served content.
2. Authors list/detail and follow toggle load backend-served content.
3. Home, library, stats, notifications, own profile, public profile, and public
   profile preview all load from backend-served content.
4. Profile edits persist for the lifetime of the running backend process.

## 4. Verify failure handling

1. Stop the backend.
2. Re-open one migrated screen from each major area.
3. Confirm the app shows explicit loading failure or empty states instead of
   falling back to embedded mock data.

## 5. Regression checks

1. Confirm login still works against the same backend origin.
2. Confirm the `back/` submodule remains isolated and no repo-linkage changes are
   required to run the feature.
3. Confirm `./gradlew testDebugUnitTest` still passes after changing the origin.
