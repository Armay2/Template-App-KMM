# Offline-first

After reading this you'll understand the three moving parts an offline-first layer needs and which of them the template ships.

## What offline-first means here

The UI renders from a local source of truth at all times. Writes are committed locally first and reconciled with the server asynchronously via a sync queue.

## What the template provides

- In-memory cache exposed as `Flow` in every repository (`TodoRepositoryImpl.cache`). This gives you the "UI renders from cache" half of the story — but only within the lifetime of the process.
- `AppException.Network` is already distinguished from other failures, so the UI can branch on "we're offline" without re-classifying errors.

## What the template does NOT provide

- Durable local storage (see `adding-persistence.md`). Without it, offline-first collapses after process death.
- Outbox / sync queue. Writes today go straight to Ktor; if the request fails, you get an `AppException` and the cache is rolled back or left stale.
- Conflict resolution. Any full implementation needs a strategy (last-write-wins, CRDT, vector clock) that matches your product semantics.

## A pragmatic recipe

1. Add SQLDelight (or Room) and move the cache from `MutableStateFlow` into a DB query `.asFlow()`.
2. Add a `PendingOperation` table: `id`, `type`, `payload (JSON)`, `created_at`, `last_error`.
3. On every mutation, write to the DB and insert a `PendingOperation` in the same transaction. Expose the result as the new Flow — the UI re-renders immediately.
4. A background coroutine (started in `TemplateApplication` / `KoinInitializer`) drains the queue, retries with exponential backoff, and removes rows on success.
5. On network recovery (observe `ConnectivityManager` or iOS `NWPathMonitor`), kick the drainer.

Expect 3–5 days of work and dedicated integration tests. Start with the feature where offline matters most and grow from there.
