# Feature Specification: Backend Rest Migration

**Feature Branch**: `backend`  
**Created**: 2026-05-12  
**Status**: Draft  
**Input**: User description: "Completar el backend tomando todos los datos mockeados hoy en shared, moverlos al backend como datos servidos por el backend, hacer que la app los consuma, mantener el backend como submódulo separado y permitir configurar la URL base con localhost como valor inicial."

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Browse Backend-Powered Content (Priority: P1)

As a FeedBook reader, I want the app sections that currently depend on embedded
sample data to load their content from the backend service so that the app
behaves like a real connected product instead of a local demo.

**Why this priority**: This is the core value of the feature. Without replacing
embedded sample data with backend-served content, the application cannot evolve
beyond a local mock experience.

**Independent Test**: Launch the app against the backend service and verify that
the main content surfaces load successfully without relying on the in-app mock
provider.

**Acceptance Scenarios**:

1. **Given** the backend service is available, **When** a reader opens the app
   sections that previously relied on embedded sample content, **Then** the app
   shows content served by the backend for each section.
2. **Given** the backend service is unavailable, **When** the reader opens one
   of those sections, **Then** the app shows a clear failure or empty-state
   response instead of silently falling back to hidden embedded data.

---

### User Story 2 - Update and Revisit Profile Data (Priority: P2)

As a FeedBook reader, I want profile information and related reading summaries
to come from the backend service and reflect my edits so that profile flows feel
consistent with the rest of the connected experience.

**Why this priority**: Profile data is one of the richest existing mock flows
and includes both read and update behavior, making it a critical proof that the
backend can support more than static browsing.

**Independent Test**: Open the profile, change editable profile fields, refresh
the profile view, and confirm the updated values are returned by the backend
during the same service session.

**Acceptance Scenarios**:

1. **Given** the reader opens their own profile, **When** profile data is
   requested, **Then** the app shows backend-served profile details, reading
   goals, streaks, current book, and related summary content.
2. **Given** the reader edits allowed profile fields, **When** the update is
   submitted, **Then** the app shows the updated data returned by the backend.

---

### User Story 3 - Point the App to a Chosen Backend Environment (Priority: P3)

As a project contributor, I want the mobile app to point to a configurable
backend base address with a local default so that I can run the app against the
backend service in development without changing source code for each environment.

**Why this priority**: The backend-backed flows are only practical if the app
can be redirected cleanly between local and future environments.

**Independent Test**: Change the configured backend base address, run the app,
and verify that content requests are sent to the selected environment without
rewiring individual screens.

**Acceptance Scenarios**:

1. **Given** a contributor is using the local backend setup, **When** the app is
   launched with the default service configuration, **Then** the app targets the
   local backend successfully.
2. **Given** a contributor needs a different backend environment, **When** they
   update the configured base address, **Then** all relevant app requests use
   the new target consistently.

### Edge Cases

- What happens when one section returns data successfully but another fails
  during the same app session?
- How does the app respond when a requested content item no longer exists in the
  backend-served dataset?
- What happens when profile edits contain invalid or incomplete values?
- How does the app behave if the configured backend address is unreachable or
  malformed for the current environment?

### Android and Data Considerations *(mandatory for FeedBook changes)*

- **Feature Boundary Impact**: This feature affects the backend submodule as the
  source of hardcoded application data and the Android feature areas that
  currently consume embedded shared mock data.
- **Network/Offline Behavior**: The app must surface loading, success, empty,
  and failure states for backend-backed sections and must not silently depend on
  hidden in-app mock data once the migration is complete.
- **Accessibility/UI Notes**: Existing screens must preserve readable, stable
  states while loading or failing so that content changes do not degrade the
  current user experience.
- **Privacy/Config Impact**: Service addresses must remain environment-specific
  configuration values, and the backend must continue treating user-facing data
  as sample data without exposing hidden credentials or unsafe defaults.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: The system MUST move the application sample datasets that are
  currently embedded in the mobile shared mock source into the backend-owned
  service layer.
- **FR-002**: The system MUST expose backend-served content for all app flows
  that currently depend on the embedded mock source, including browsing,
  discovery, social activity, reading statistics, profile summaries, and
  related book details.
- **FR-003**: The mobile app MUST retrieve those datasets from the backend
  service instead of reading them directly from embedded shared mock content.
- **FR-004**: The mobile app MUST provide user-visible loading and failure
  states whenever backend-served content cannot be returned.
- **FR-005**: The system MUST support retrieving both the reader's own profile
  data and a public profile view from the backend-served dataset.
- **FR-006**: The system MUST allow editable profile fields to be submitted from
  the mobile app and reflected in subsequent reads during the same backend
  runtime.
- **FR-007**: The backend-owned dataset MAY remain hardcoded and non-persistent
  for this phase, provided the behavior is consistent across requests while the
  backend process is running.
- **FR-008**: The mobile app MUST use a single configurable backend base address
  for the migrated content flows, with a local development default available at
  first run.
- **FR-009**: The feature MUST preserve the existing backend submodule boundary
  so that backend implementation work remains isolated from submodule linkage and
  repository ownership concerns.
- **FR-010**: The app MUST avoid hidden fallback behavior that would mask a
  missing backend integration by continuing to serve stale embedded mock data.
- **FR-011**: The backend-served sample content MUST remain structured enough to
  support the same user journeys already represented in the mobile experience.

### Key Entities *(include if feature involves data)*

- **Backend Content Catalog**: The service-owned collection of books, reviews,
  reading progress, author information, notifications, home content, library
  summaries, and statistics currently shown in the app.
- **Profile Snapshot**: The reader profile view returned by the backend,
  including editable identity fields plus reading goal, streak, current book,
  library highlights, and featured reviews.
- **Service Configuration**: The environment-specific base address the mobile app
  uses to reach the backend for migrated content requests.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: 100% of app sections previously backed by the embedded shared mock
  source can be opened and populated using backend-served data during local
  verification.
- **SC-002**: In a local development setup, contributors can point the app to
  the intended backend environment in under 2 minutes without editing multiple
  screens or feature-specific data sources.
- **SC-003**: When the backend is unavailable, 100% of migrated app sections
  show an explicit user-facing loading failure or empty state instead of silent
  mock fallback behavior.
- **SC-004**: Profile edits submitted through the app are visible on a refreshed
  profile read during the same backend service session in all validation runs.

## Assumptions

- The backend remains a separate submodule and repository, and this feature only
  extends its application behavior rather than changing its repository
  relationship.
- Hardcoded backend data is acceptable for this phase as long as it is stable,
  internally consistent, and sufficient to drive the current app flows.
- Existing login behavior can remain available alongside the new backend-served
  content flows and does not require a new persistence layer in this phase.
- The current mobile screens and user journeys remain in scope; the feature is
  about changing the source of truth and integration behavior, not redesigning
  those experiences.

## Verification Strategy *(mandatory)*

- **Unit Tests**: Validate data mapping, error handling, and configuration
  behavior where content is translated or routed between the service and the app.
- **Integration/UI Tests**: Verify that migrated app sections, profile reads and
  updates, and service configuration changes work end-to-end against the backend
  service.
- **Manual Validation**: Run the app against the local backend default, browse
  each migrated section, update the profile, and confirm visible failure states
  when the backend is intentionally unavailable.
