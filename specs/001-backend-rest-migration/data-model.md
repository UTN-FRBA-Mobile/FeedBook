# Data Model: Backend Rest Migration

## BackendContentCatalog

- **Purpose**: Aggregate all currently embedded sample content that must move to
  the backend-owned source of truth.
- **Contains**:
  - books
  - readingProgressByBookId
  - reviewsByBookId
  - exploreUsers
  - authors
  - ownProfile
  - publicProfile
  - homeFeed
  - ownLibrary
  - stats
  - notifications
- **Rules**:
  - All collections are hardcoded at process start.
  - Content remains stable across requests unless explicitly mutated by a
    supported write operation.
  - IDs must remain unique within each collection and consistent across related
    responses.

## ProfileSnapshot

- **Purpose**: Represent the reader-facing profile payload returned to the app
  for own profile, public preview, and public profile flows.
- **Fields**:
  - identity: name, handle, quote
  - avatar: top color, bottom color, preset id, preset image URL, optional image URI
  - availableAvatarPresets
  - readingGoal
  - readingStreak
  - currentBook
  - upNextBooks
  - completedBooks
  - profileStats
  - publicLibrary
  - featuredReviews
- **State transitions**:
  - `GET /api/profile/me` returns the current in-memory own profile snapshot.
  - `PUT /api/profile/me` validates the editable subset and replaces the in-memory
    own profile snapshot fields.
  - `GET /api/profile/me/preview` returns a public-safe projection of the own
    profile snapshot.
  - `GET /api/profile/public` returns the static public profile snapshot.
- **Validation rules**:
  - `name`, `handle`, and `quote` cannot be blank after trimming.
  - `targetPagesPerDay`, when present, must be positive.
  - Avatar colors and preset references must remain internally consistent.

## HomeFeedSnapshot

- **Purpose**: Serve the app home feed as one backend response.
- **Fields**:
  - trendingTitle
  - avatar
  - featuredBook
  - rankedBooks
  - readingRooms
  - curators
- **Rules**:
  - Response is derived from static content plus the current own-profile avatar
    when needed to preserve the current product feel.

## ReaderLibrarySnapshot

- **Purpose**: Serve the reader library overview and history screen.
- **Fields**:
  - title
  - subtitle
  - avatar
  - currentBook
  - readingBooks
  - shelfBooks
  - completedBooks
  - readHistory
- **Rules**:
  - Can be derived from own-profile state where the existing fake backend already
    shares data between profile and library.

## BookAggregate

- **Purpose**: Support browse and detail flows for books.
- **Fields**:
  - core book record
  - optional reading progress
  - zero or more reviews
- **Relationships**:
  - One book may have many reviews.
  - One book may have zero or one current reading-progress entry in this phase.

## AuthorAggregate

- **Purpose**: Support author list, detail, and follow/unfollow behavior.
- **Fields**:
  - author metadata
  - nested authored books
  - follower count
  - follow state indicator if needed by the mobile model
- **State transitions**:
  - `GET /api/authors` returns all author summaries.
  - `GET /api/authors/{id}` returns one aggregate.
  - `POST /api/authors/{id}/follow-toggle` mutates the in-memory follow state and
    resulting counters/flags.

## NotificationsFeed

- **Purpose**: Represent the social activity timeline shown in the notifications
  screen.
- **Fields**:
  - title
  - items
  - actor summary
  - optional book summary
  - fallback text
- **Rules**:
  - Notification type values must remain stable because the app maps them to UI
    treatments.

## StatsSnapshot

- **Purpose**: Provide the reading statistics view as one request.
- **Fields**:
  - title
  - subtitle
  - metrics
  - heatmapMonths
  - heatmapRows
  - heatmapValues
  - radarSections
- **Rules**:
  - Heatmap grids and radar sections must remain shape-compatible with the
    current UI rendering assumptions.

## ServiceConfiguration

- **Purpose**: Represent the app-side selection of the backend server origin.
- **Fields**:
  - backendOrigin
  - contentBasePath
  - authBasePath
- **Rules**:
  - Default origin points to localhost-compatible development setup.
  - All migrated content requests derive from this configuration instead of
    embedding per-feature origins.
