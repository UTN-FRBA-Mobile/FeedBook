# Research: Backend Rest Migration

## Decision 1: Keep the backend as a self-contained HTTP module inside `back/`

- **Decision**: Implement the new content API entirely within the `back/`
  submodule using Go standard-library routing and an internal package split for
  models, store, service, and HTTP handlers.
- **Rationale**: The user explicitly wants the backend completed without
  breaking the fact that it is a separate submodule/repository. A light internal
  layering keeps the service maintainable while preserving submodule ownership.
- **Alternatives considered**:
  - Expand `main.go` into a single file with all handlers. Rejected because the
    content surface is too large and would become hard to test or extend.
  - Move data/service logic into the Android repo outside the submodule.
    Rejected because it breaks the submodule boundary.

## Decision 2: Use snapshot-style REST endpoints, not streaming or websockets

- **Decision**: Expose the migrated datasets through standard request/response
  REST endpoints and keep all content in memory for the process lifetime.
- **Rationale**: The current product need is to serve static mock data plus
  in-session profile edits. Snapshot endpoints satisfy all current screens
  without adding operational complexity.
- **Alternatives considered**:
  - Server-sent events or websocket updates. Rejected because no live push
    behavior is required by the spec.
  - File-backed persistence. Rejected because the feature explicitly allows
    hardcoded non-persistent data.

## Decision 3: Preserve Android repository flows by backing them with refreshable local state

- **Decision**: Keep `Flow`-based repository APIs in `home`, `library`, and
  `profile`, but hydrate those flows from Retrofit snapshot requests via local
  in-memory state within the app layer.
- **Rationale**: This keeps the existing domain and presentation contracts stable
  while allowing backend-driven content. It also avoids forcing streaming support
  into the backend solely to mimic the old fake source.
- **Alternatives considered**:
  - Rewrite domain repositories to synchronous one-shot APIs. Rejected because it
    would ripple unnecessary changes through view models and screens.
  - Keep the fake backend only for flow-based sections. Rejected because it
    violates the requirement to remove hidden mock fallback behavior.

## Decision 4: Standardize on a single backend origin configuration

- **Decision**: Introduce one environment-configurable backend origin for the Go
  server and derive both authentication and content requests from that origin in
  the Android network layer.
- **Rationale**: The feature asks for a configurable base URL with a localhost
  default. A single origin reduces configuration drift and makes local setup
  predictable.
- **Alternatives considered**:
  - Keep separate content and auth origins indefinitely. Rejected because it
    complicates configuration and increases the chance of partial misconfiguration.
  - Hardcode localhost-only values in multiple places. Rejected because it does
    not scale to future environments.

## Decision 5: Use contract-first parity with current DTO shapes

- **Decision**: Design backend responses to match the Android DTO structures
  already consumed by the app wherever practical, including nested payloads for
  profile, home, library, stats, notifications, authors, books, reviews, and
  reading progress.
- **Rationale**: Matching the current DTO contract minimizes mapper churn and
  reduces migration risk while still allowing internal backend restructuring.
- **Alternatives considered**:
  - Redesign payloads and remap every feature. Rejected because it increases
    scope without adding user value to this phase.
  - Return one monolithic endpoint for all data. Rejected because it couples
    unrelated screens and hurts partial failure handling.
