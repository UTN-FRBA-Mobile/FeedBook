# Implementation Plan: Backend Rest Migration

**Branch**: `backend` | **Date**: 2026-05-12 | **Spec**: [spec.md](./spec.md)
**Input**: Feature specification from `/specs/001-backend-rest-migration/spec.md`

**Note**: This plan covers a split implementation across the Android app in this
repository and the Go backend that remains inside the `back/` submodule.

## Summary

Replace the in-app `shared/fakebackend` sample data source with a Go backend that
serves the same product content through REST endpoints, then rewire the Android
data layer to consume those endpoints using a single configurable backend origin.
The backend keeps hardcoded in-memory content for this phase, while the mobile
app preserves its existing feature-oriented domain APIs, loading states, and
profile-edit behavior.

## Technical Context

**Language/Version**: Kotlin 2.2.10 for Android app; Go 1.26.0 for backend  
**Primary Dependencies**: Jetpack Compose, AndroidX Lifecycle, Retrofit,
Gson, OkHttp, Kotlin Coroutines; Go standard library `net/http`  
**Storage**: In-memory hardcoded datasets only; no persistence  
**Testing**: JUnit4 and Android instrumentation in app; Go `testing` with HTTP
handler tests in backend  
**Target Platform**: Android 9+ client and local Go HTTP backend on developer
machines  
**Project Type**: Mobile app plus HTTP API submodule  
**Performance Goals**: Local content endpoints should feel instantaneous during
manual testing and return complete payloads within normal local-network latency  
**Constraints**: Preserve `back/` as a submodule, avoid hidden mock fallback,
support localhost-first configuration, keep UI contracts stable for existing
screens  
**Scale/Scope**: One Android app module, one Go backend module, existing reading,
profile, library, stats, notifications, authors, and books flows

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

- **Architecture**: Pass. Android changes stay inside existing feature packages,
  `core/network`, and DI wiring. Shared mock content is removed from `shared/`
  and re-homed into the backend submodule without breaking submodule boundaries.
- **Network/Offline**: Pass. The backend becomes the only source of truth for
  migrated content. Android repositories keep explicit loading/error handling and
  will not silently fall back to embedded data.
- **Testing**: Pass. Plan includes Go endpoint tests plus Android mapper/data
  layer tests and targeted integration validation for profile update and content
  loading.
- **UI Consistency**: Pass. Existing Compose screens and UI models remain the
  presentation contract; only data sourcing and error/loading handling are
  adjusted.
- **Configuration/Privacy**: Pass. Backend origin becomes environment-driven
  config with localhost default, and no new sensitive storage or secret handling
  is introduced.

## Project Structure

### Documentation (this feature)

```text
specs/001-backend-rest-migration/
├── plan.md
├── research.md
├── data-model.md
├── quickstart.md
├── contracts/
│   └── openapi.yaml
└── tasks.md
```

### Source Code (repository root)

```text
back/
├── main.go
├── main_test.go
└── internal/
    └── feedbook/
        ├── models.go
        ├── store.go                 # planned static dataset and mutations
        ├── service.go               # planned business/query layer
        └── http/
            ├── handlers.go          # planned REST handlers
            └── routes.go            # planned route registration

app/
├── build.gradle.kts
└── src/
    ├── main/java/com/example/feedbook/
    │   ├── core/network/
    │   ├── core/di/
    │   ├── features/
    │   │   ├── authors/
    │   │   ├── books/
    │   │   ├── home/
    │   │   ├── library/
    │   │   ├── notifications/
    │   │   ├── profile/
    │   │   └── stats/
    │   └── shared/
    ├── test/
    └── androidTest/
```

**Structure Decision**: Keep the backend implementation fully inside the `back/`
submodule and keep Android changes inside the existing feature-based module
structure. Introduce a small backend HTTP layering (`store/service/http`) to
avoid `main.go` becoming the permanent data source and routing hub. On Android,
replace direct `FakeFeedBookBackend` usage with Retrofit-backed remote data
sources while preserving domain repository interfaces.

## Complexity Tracking

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| None | N/A | N/A |
