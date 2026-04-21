# ADR 0005: Throw `AppException` rather than return `Result<T, E>`

- **Status:** Accepted
- **Date:** 2026-04-21

## Context

The two idiomatic Kotlin error-handling styles for repositories / use-cases are: (a) throw a sealed exception; (b) return a monadic result type (`Result<T>`, `Either<E, T>`, `kotlin.Result`). Result types avoid hidden control flow but force every caller to unwrap, pollute signatures, and compose badly with `Flow` / `suspend` chains. Kotlin's own `kotlin.Result` explicitly excludes cross-suspend use. Swift interop also matters: SKIE maps `@Throws` declarations to idiomatic Swift `async throws`, but there's no equivalent for a Kotlin `Result<T>` — callers would see a raw enum-like wrapper.

## Decision

Every public `suspend fun` that can fail throws an `AppException` subclass (`Network`, `NotFound`, `Unauthorized`, `Validation`, `Unknown`). Declare it with `@Throws(AppException::class, CancellationException::class)` on the interface and impl. HTTP failures are translated at the data-layer boundary by `Throwable.toAppException()`. ViewModels catch `AppException` and either set `UiStatus.Error` in state or emit a `ShowError` side-effect. `CancellationException` always rethrows (`ErrorMapper.map`).

## Consequences

**Positive.** Call sites stay readable (`repo.toggle(id)` instead of `repo.toggle(id).fold(...)`). SKIE turns `@Throws` into Swift `async throws`, matching what iOS developers expect. Flow pipelines don't have to thread a result envelope. Tests assert exception types with `assertFailsWith<AppException.Network>`.

**Negative.** A caller that forgets to `try/catch` can crash — the compiler won't force the check the way a `Result<T>` would. Mitigated by the `@Throws` annotation being visible in autocomplete, by every ViewModel having a catch block as a reviewed pattern, and by linting via detekt's `TooGenericExceptionCaught` where useful. Net: idiomatic Kotlin, idiomatic Swift, and readable call sites beat monadic purity.
