---

description: "Task list for Backend Rest Migration feature implementation"

---

# Tasks: Backend Rest Migration

**Input**: Design documents from `/specs/001-backend-rest-migration/`
**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/openapi.yaml

**Tests**: Automated coverage is required for this feature. Add Go handler tests
and Android unit/integration coverage for backend-backed content, profile
updates, and backend origin configuration.

**Organization**: Tasks are grouped by user story to enable independent
implementation and testing of each story.

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (e.g., US1, US2, US3)
- Include exact file paths in descriptions

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Prepare the backend and Android codebases for the migration without
changing behavior yet.

- [X] T001 Create the feature task scaffolding in `specs/001-backend-rest-migration/tasks.md`
- [X] T002 [P] Add backend HTTP package entry files in `back/internal/feedbook/http/routes.go` and `back/internal/feedbook/http/handlers.go`
- [X] T003 [P] Add backend service-layer placeholders in `back/internal/feedbook/store.go` and `back/internal/feedbook/service.go`
- [X] T004 [P] Add Android backend origin configuration placeholders in `app/build.gradle.kts` and `app/src/main/java/com/example/feedbook/core/network/NetworkModule.kt`

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Build the shared infrastructure that every user story depends on.

**⚠️ CRITICAL**: No user story work can begin until this phase is complete

- [X] T005 Implement the backend static content store and mutation-safe accessors in `back/internal/feedbook/store.go`
- [X] T006 Implement the backend query/update service layer in `back/internal/feedbook/service.go`
- [X] T007 Wire the HTTP router, shared JSON helpers, and error handling in `back/internal/feedbook/http/routes.go`, `back/internal/feedbook/http/handlers.go`, and `back/main.go`
- [X] T008 Implement a single configurable backend origin for app networking in `app/build.gradle.kts`, `app/src/main/java/com/example/feedbook/core/network/NetworkModule.kt`, and `app/src/main/java/com/example/feedbook/core/network/AuthApiService.kt`
- [X] T009 Replace direct `FakeFeedBookBackend` injection with Retrofit-oriented dependency wiring in `app/src/main/java/com/example/feedbook/core/di/` and `app/src/main/java/com/example/feedbook/core/network/ApiService.kt`

**Checkpoint**: Foundation ready - user story implementation can now begin

---

## Phase 3: User Story 1 - Browse Backend-Powered Content (Priority: P1) 🎯 MVP

**Goal**: Serve all previously embedded browse/discovery/library/stats/social
content from the backend and consume it from the Android app without hidden mock
fallback.

**Independent Test**: Run the backend and verify that books, authors, home,
library, stats, notifications, and related detail screens load backend-served
content. Stop the backend and verify those screens show explicit failure states.

### Tests for User Story 1 ⚠️

- [X] T010 [P] [US1] Add backend contract tests for browse endpoints in `back/internal/feedbook/http/handlers_test.go`
- [X] T011 [P] [US1] Add Android data-layer tests for backend browse mappers and error handling in `app/src/test/java/com/example/feedbook/features/books/`, `app/src/test/java/com/example/feedbook/features/authors/`, `app/src/test/java/com/example/feedbook/features/home/`, `app/src/test/java/com/example/feedbook/features/library/`, `app/src/test/java/com/example/feedbook/features/stats/`, and `app/src/test/java/com/example/feedbook/features/notifications/`
- [ ] T012 [P] [US1] Add Android integration coverage for backend-backed browse flows in `app/src/androidTest/java/com/example/feedbook/`

### Implementation for User Story 1

- [X] T013 [P] [US1] Move book, review, reading progress, and explore-user datasets out of `app/src/main/java/com/example/feedbook/shared/fakebackend/FakeFeedBookBackend.kt` into `back/internal/feedbook/store.go` and align schemas in `back/internal/feedbook/models.go`
- [X] T014 [P] [US1] Move author, home, library, stats, and notifications datasets out of `app/src/main/java/com/example/feedbook/shared/fakebackend/FakeFeedBookBackend.kt` into `back/internal/feedbook/store.go` and align schemas in `back/internal/feedbook/models.go`
- [X] T015 [US1] Implement browse endpoint handlers for `/api/books`, `/api/books/{bookId}`, `/api/books/{bookId}/progress`, `/api/books/{bookId}/reviews`, `/api/explore/users`, `/api/authors`, `/api/authors/{authorId}`, `/api/authors/{authorId}/follow-toggle`, `/api/home`, `/api/library/me`, `/api/stats`, and `/api/notifications` in `back/internal/feedbook/http/handlers.go`
- [X] T016 [P] [US1] Rewire book and author remote data sources to Retrofit in `app/src/main/java/com/example/feedbook/features/books/data/remote/BookRemoteDataSource.kt` and `app/src/main/java/com/example/feedbook/features/authors/data/remote/AuthorRemoteDataSource.kt`
- [X] T017 [P] [US1] Rewire home, library, stats, and notifications remote data sources to Retrofit-backed snapshot loading in `app/src/main/java/com/example/feedbook/features/home/data/remote/HomeRemoteDataSource.kt`, `app/src/main/java/com/example/feedbook/features/library/data/remote/LibraryRemoteDataSource.kt`, `app/src/main/java/com/example/feedbook/features/stats/data/remote/StatsRemoteDataSource.kt`, and `app/src/main/java/com/example/feedbook/features/notifications/data/remote/NotificationsRemoteDataSource.kt`
- [ ] T018 [US1] Adapt repositories and view-model-facing refresh/state logic for backend browse content in `app/src/main/java/com/example/feedbook/features/home/data/repository/HomeRepositoryImpl.kt`, `app/src/main/java/com/example/feedbook/features/library/data/repository/LibraryRepositoryImpl.kt`, `app/src/main/java/com/example/feedbook/features/stats/data/repository/StatsRepositoryImpl.kt`, `app/src/main/java/com/example/feedbook/features/notifications/data/repository/NotificationsRepositoryImpl.kt`, `app/src/main/java/com/example/feedbook/features/books/data/repository/BookRepositoryImpl.kt`, and `app/src/main/java/com/example/feedbook/features/authors/data/repository/AuthorRepositoryImpl.kt`
- [X] T019 [US1] Remove hidden browse-content fallback paths and obsolete fake-backend wiring from `app/src/main/java/com/example/feedbook/shared/fakebackend/FakeFeedBookBackend.kt` and all migrated remote data sources under `app/src/main/java/com/example/feedbook/features/`

**Checkpoint**: User Story 1 should be fully functional and testable
independently as the MVP

---

## Phase 4: User Story 2 - Update and Revisit Profile Data (Priority: P2)

**Goal**: Back the own-profile, public-profile, preview, and profile-edit flows
with backend-served state that persists for the backend process lifetime.

**Independent Test**: Run the backend, load own profile and public profile,
submit profile edits, refresh the views, and confirm the updated values are
returned from backend-served state in the same session.

### Tests for User Story 2 ⚠️

- [X] T020 [P] [US2] Add backend contract tests for `/api/profile/me`, `/api/profile/me/preview`, and `/api/profile/public` in `back/internal/feedbook/http/handlers_test.go`
- [X] T021 [P] [US2] Add Android repository/unit tests for profile read-update-refresh behavior in `app/src/test/java/com/example/feedbook/features/profile/`
- [ ] T022 [P] [US2] Add Android integration coverage for profile update and reload flows in `app/src/androidTest/java/com/example/feedbook/`

### Implementation for User Story 2

- [X] T023 [P] [US2] Move own-profile and public-profile datasets plus update validation rules out of `app/src/main/java/com/example/feedbook/shared/fakebackend/FakeFeedBookBackend.kt` into `back/internal/feedbook/store.go` and `back/internal/feedbook/service.go`
- [X] T024 [US2] Implement profile read, preview, public, and update handlers in `back/internal/feedbook/http/handlers.go`
- [X] T025 [US2] Rewire the profile remote data source to backend snapshot requests in `app/src/main/java/com/example/feedbook/features/profile/data/remote/ProfileRemoteDataSource.kt`
- [ ] T026 [US2] Adapt profile repositories and refreshable local state handling in `app/src/main/java/com/example/feedbook/features/profile/data/repository/ProfileRepositoryImpl.kt`
- [ ] T027 [US2] Update profile view-model flows to surface backend loading and failure states consistently in `app/src/main/java/com/example/feedbook/features/profile/presentation/ProfileViewModel.kt`, `app/src/main/java/com/example/feedbook/features/profile/presentation/PublicProfileViewModel.kt`, `app/src/main/java/com/example/feedbook/features/profile/presentation/PublicProfilePreviewViewModel.kt`, and `app/src/main/java/com/example/feedbook/features/profile/presentation/EditProfileViewModel.kt`

**Checkpoint**: User Stories 1 and 2 should both work independently with
backend-served profile state

---

## Phase 5: User Story 3 - Point the App to a Chosen Backend Environment (Priority: P3)

**Goal**: Make the backend origin configurable with a localhost-first default so
contributors can switch environments without recoding feature integrations.

**Independent Test**: Change the configured backend origin, launch the app, and
confirm both login and migrated content flows target the selected environment.

### Tests for User Story 3 ⚠️

- [X] T028 [P] [US3] Add unit tests for backend-origin configuration and URL derivation in `app/src/test/java/com/example/feedbook/core/network/`
- [X] T029 [P] [US3] Add validation coverage for backend startup/config defaults in `back/main_test.go`

### Implementation for User Story 3

- [X] T030 [US3] Replace split auth/content base URL usage with a single backend origin contract in `app/build.gradle.kts`, `app/src/main/java/com/example/feedbook/core/network/ApiService.kt`, `app/src/main/java/com/example/feedbook/core/network/AuthApiService.kt`, and `app/src/main/java/com/example/feedbook/core/network/NetworkModule.kt`
- [X] T031 [US3] Update backend startup configuration and developer-facing defaults in `back/main.go` and `back/README.md`
- [X] T032 [US3] Document environment switching and local setup in `specs/001-backend-rest-migration/quickstart.md` and `README.md`

**Checkpoint**: All user stories should now be independently functional

---

## Phase 6: Polish & Cross-Cutting Concerns

**Purpose**: Finish migration cleanup, regression coverage, and end-to-end
validation across stories.

- [X] T033 [P] Remove the now-obsolete mobile fake backend implementation and related references in `app/src/main/java/com/example/feedbook/shared/fakebackend/FakeFeedBookBackend.kt` and `app/src/main/java/com/example/feedbook/core/di/`
- [ ] T034 Run end-to-end validation from `specs/001-backend-rest-migration/quickstart.md` and capture any required fixes in `specs/001-backend-rest-migration/quickstart.md`
- [X] T035 [P] Update project documentation for the backend-backed architecture in `README.md`, `AGENTS.md`, and `specs/001-backend-rest-migration/contracts/openapi.yaml`

---

## Dependencies & Execution Order

### Phase Dependencies

- **Setup (Phase 1)**: No dependencies; can start immediately
- **Foundational (Phase 2)**: Depends on Setup completion; blocks all user
  stories
- **User Story 1 (Phase 3)**: Depends on Foundational completion; forms the MVP
- **User Story 2 (Phase 4)**: Depends on Foundational completion and benefits
  from User Story 1 endpoint/repository patterns
- **User Story 3 (Phase 5)**: Depends on Foundational completion and should be
  verified against completed network integrations
- **Polish (Phase 6)**: Depends on all desired user stories being complete

### User Story Dependencies

- **User Story 1 (P1)**: Can start after Phase 2 and is the recommended MVP
- **User Story 2 (P2)**: Can start after Phase 2, but is easiest after User
  Story 1 establishes backend/service and Retrofit patterns
- **User Story 3 (P3)**: Can start after Phase 2, but final validation depends
  on migrated content and login using the shared backend origin

### Within Each User Story

- Tests required by the spec and constitution MUST be added with the story and
  completed before sign-off
- Backend dataset/service work must precede handler implementation
- Handler/API work must precede Android remote data source rewiring
- Remote data source rewiring must precede repository and presentation refresh
  behavior
- Story completion requires validation of both success and failure states

### Parallel Opportunities

- `T002`, `T003`, and `T004` can run in parallel during setup
- `T005` and `T008` can progress in parallel once setup is done
- `T013` and `T014` can run in parallel while preparing migrated backend data
- `T016` and `T017` can run in parallel once browse endpoints exist
- `T020`, `T021`, and `T022` can run in parallel in User Story 2
- `T028` and `T029` can run in parallel in User Story 3
- `T033` and `T035` can run in parallel during polish

---

## Parallel Example: User Story 1

```bash
# Launch User Story 1 test work together:
Task: "T010 Add backend contract tests for browse endpoints in back/internal/feedbook/http/handlers_test.go"
Task: "T011 Add Android data-layer tests in app/src/test/java/com/example/feedbook/features/books/ and sibling feature packages"
Task: "T012 Add Android integration coverage in app/src/androidTest/java/com/example/feedbook/"

# Launch User Story 1 data migration work together:
Task: "T013 Move book/review/progress/explore datasets into back/internal/feedbook/store.go"
Task: "T014 Move author/home/library/stats/notifications datasets into back/internal/feedbook/store.go"
```

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Complete Phase 1: Setup
2. Complete Phase 2: Foundational
3. Complete Phase 3: User Story 1
4. **STOP and VALIDATE**: Verify all migrated browse/content flows load from the
   backend and fail visibly when the backend is down

### Incremental Delivery

1. Finish Setup + Foundational to establish the backend/service and app network
   baseline
2. Deliver User Story 1 as the MVP for backend-served content
3. Deliver User Story 2 for backend-served profile reads and in-session updates
4. Deliver User Story 3 to finalize environment switching and local setup
5. Finish with Phase 6 cleanup and regression validation

### Parallel Team Strategy

With multiple developers:

1. One developer prepares backend store/service/router foundations
2. One developer prepares Android network/DI foundations
3. After Phase 2:
   - Developer A: backend browse/profile handlers
   - Developer B: Android browse data source and repository rewiring
   - Developer C: configuration, docs, and validation support

---

## Notes

- [P] tasks = different files, no dependencies on incomplete tasks
- [US1], [US2], and [US3] labels map directly to the user stories in `spec.md`
- Every story is independently testable from the quickstart flow
- The backend submodule boundary is preserved throughout; tasks modify code
  inside `back/` rather than relocating backend ownership
- Suggested MVP scope: Phase 1 + Phase 2 + Phase 3
