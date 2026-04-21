# ADR 0003: Settings only, no bundled database

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

A template can either ship an opinionated database (SQLDelight or Room KMP) or leave persistence out. Shipping a database gives new apps a head start but forces a schema, a migration story, and a driver choice on every consumer — even those building ephemeral UIs, chat clients, or pure-API browsers. Schema design is deeply app-specific and the cost of undoing a bundled default is higher than the cost of adding one later.

We still need a place to store small scalars — auth tokens, feature flags, onboarding completion, selected theme — on both platforms.

## Decision

Ship only `multiplatform-settings` wrapped in a small `KeyValueStore` abstraction (`shared/src/commonMain/kotlin/com/electra/template/data/storage/KeyValueStore.kt`). Expose a common `SettingsFactory` interface with platform classes `AndroidSettingsFactory` (backed by `SharedPreferencesSettings`) and `IosSettingsFactory` (backed by `NSUserDefaultsSettings`), wired via `androidPlatformModule` / `iosPlatformModule`. Data lists (e.g. `Todo`) live in an in-memory `MutableStateFlow` inside the repository and are refetched on startup. Document the SQLDelight / Room KMP upgrade path in `docs/06-extension-paths/adding-persistence.md`.

## Consequences

**Positive.** Zero schema to maintain in the template, zero migration tests to write at day zero, zero additional build time. Any team can add SQLDelight the day they need it. Tokens and flags work out of the box.

**Negative.** Offline-first is not possible without adding a DB; the template flags this clearly in `docs/06-extension-paths/offline-first.md`. In-memory caches are lost at process death, which surprises engineers who haven't read the architecture doc. Mitigated by keeping the repository interface unchanged when DB support arrives, so migration is isolated to `*RepositoryImpl.kt`.
