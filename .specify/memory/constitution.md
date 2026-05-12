<!--
Sync Impact Report
- Version change: template -> 1.0.0
- Modified principles:
  - Template Principle 1 -> I. Feature-Scoped Android Architecture
  - Template Principle 2 -> II. Explicit Network and Offline Boundaries
  - Template Principle 3 -> III. Testable Behavior Is Mandatory
  - Template Principle 4 -> IV. Accessible and Consistent Compose UI
  - Template Principle 5 -> V. Safe Configuration and Privacy Defaults
- Added sections:
  - Delivery Constraints
  - Development Workflow
- Removed sections:
  - None
- Templates requiring updates:
  - ✅ updated: .specify/templates/plan-template.md
  - ✅ updated: .specify/templates/spec-template.md
  - ✅ updated: .specify/templates/tasks-template.md
  - ✅ updated: README.md
- Follow-up TODOs:
  - None
-->
# FeedBook Constitution

## Core Principles

### I. Feature-Scoped Android Architecture
Every product change MUST live inside the existing Android feature-oriented
structure under `app/src/main/java/com/example/feedbook`, using `core/` only for
cross-feature infrastructure and `shared/` only for intentionally shared models
or test/fake implementations. New dependencies between features MUST flow through
stable interfaces or shared domain models rather than direct screen-to-screen
coupling. Rationale: FeedBook is already organized by feature packages; keeping
that boundary intact prevents UI, navigation, and data concerns from collapsing
into a single unmaintainable module.

### II. Explicit Network and Offline Boundaries
Any behavior that reads or writes remote data MUST define its loading, success,
and failure states in the user-facing flow and MUST keep transport details
inside network or data-layer code. Features that touch local persistence,
downloaded media, or cached progress MUST document how they behave without
connectivity and how stale data is surfaced to the user. Rationale: FeedBook
combines device capabilities, local state, and remote services, so ambiguity at
those boundaries creates broken reading flows and hard-to-reproduce bugs.

### III. Testable Behavior Is Mandatory
Every non-trivial change MUST ship with automated verification proportional to
its risk: pure logic requires unit tests, stateful view models or repositories
require focused tests, and changes to navigation, authentication, network
integration, or critical reading flows MUST include integration or
instrumentation coverage. A task or pull request is incomplete until the new
behavior has a failing-or-missing test gap addressed or an explicit written
justification for why automated coverage is infeasible. Rationale: the project
already spans Android UI and backend behavior, so regressions become expensive
unless new logic is locked down where it changes.

### IV. Accessible and Consistent Compose UI
Compose screens and components MUST use the shared theme and reusable UI models
whenever possible, MUST provide meaningful content descriptions or equivalent
accessibility semantics for non-decorative elements, and MUST preserve readable
contrast and resilient layouts across supported Android form factors. Any new
visual pattern MUST either extend the existing design language or deliberately
document why a new pattern is required. Rationale: FeedBook is a reading-heavy
app; consistency and accessibility are part of core product quality, not polish.

### V. Safe Configuration and Privacy Defaults
Secrets, tokens, and environment-specific endpoints MUST stay out of committed
source files unless they are clearly fake development values, and release-ready
flows MUST NOT depend on localhost-only infrastructure without an explicit
fallback or deployment plan. Authentication, biometric, profile, and reading
activity data MUST be handled with least-privilege access and only logged in
redacted or non-sensitive form. Rationale: the app already includes auth,
biometrics, and social reading data, so unsafe defaults would turn classroom
shortcuts into production-grade security problems.

## Delivery Constraints

- The supported client platform is Android 9 (API 28) or higher using Kotlin and
  Jetpack Compose in the `:app` module.
- Feature work MUST document any required backend contract, fake backend change,
  or device capability dependency before implementation begins.
- New third-party libraries SHOULD be added only when platform or existing
  project APIs cannot reasonably solve the problem; the plan MUST capture the
  tradeoff.
- User-visible metrics, stats, or reading progress calculations MUST define the
  source of truth and rounding/display rules in the specification.

## Development Workflow

- Specifications MUST describe the affected user journey, failure states,
  accessibility considerations, and any network/offline assumptions before work
  is planned.
- Implementation plans MUST pass a Constitution Check that maps the change to
  feature boundaries, testing depth, configuration/privacy impact, and UI
  consistency requirements.
- Tasks MUST be grouped by user story and include the concrete verification work
  required by Principle III whenever behavior changes.
- Reviews MUST block on unresolved constitution violations unless the exception,
  justification, and follow-up owner are documented in the plan or task set.

## Governance

This constitution overrides conflicting local habits for product, Android, and
backend work in this repository. Amendments require: (1) the proposed text
change, (2) the reason the current rule is insufficient, (3) updates to any
affected templates or guidance files, and (4) a semantic version decision
recorded in the Sync Impact Report. Versioning follows semantic rules for this
document: MAJOR for incompatible governance changes or principle removals, MINOR
for new principles or materially expanded obligations, and PATCH for
clarifications that do not change expected behavior. Every feature plan and
implementation review MUST include a constitution compliance check covering
architecture boundaries, test coverage, UI consistency/accessibility, and
configuration/privacy risk.

**Version**: 1.0.0 | **Ratified**: 2026-05-12 | **Last Amended**: 2026-05-12
