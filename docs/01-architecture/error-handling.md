# Error handling

After reading this you'll know the `AppException` hierarchy, where errors are mapped from HTTP, and how they surface in `UiStatus`.

## The sealed hierarchy

`shared/src/commonMain/kotlin/com/electra/template/core/error/AppException.kt`:

```kotlin
sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class Network(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class NotFound(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Unauthorized(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Validation(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Unknown(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
}
```

## Where errors are thrown

Repositories and use-cases `throw` — we don't use `Result<T, E>` (see ADR 0005). Every public `suspend fun` exposed to iOS carries `@Throws(AppException::class, CancellationException::class)` so SKIE generates Swift `throws` signatures. See `TodoRepository.kt` and `TodoRepositoryImpl.kt`.

HTTP status → `AppException` mapping lives in `shared/src/commonMain/kotlin/com/electra/template/data/network/ErrorInterceptor.kt` (`Throwable.toAppException()`): 404 → `NotFound`, 401 → `Unauthorized`, 400/422 → `Validation`, 5xx → `Network`.

For catch blocks that must forward cancellation, `ErrorMapper.map` rethrows `CancellationException` before mapping everything else to `AppException.Unknown`.

## Where errors surface

ViewModels catch `AppException`, then either update `state.status` with `UiStatus.Error(message, retryable)` or emit a `ShowError` side-effect:

```kotlin
catch (e: AppException) {
    update { it.copy(status = UiStatus.Error(e.message ?: "error", retryable = true)) }
    emit(TodoListSideEffect.ShowError(e.message ?: "error"))
}
```

`UiStatus` (`shared/src/commonMain/kotlin/com/electra/template/presentation/base/UiStatus.kt`) has `Idle` / `Loading` / `Error` states and is what your views branch on to render spinners or error banners.
