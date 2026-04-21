# Adding authentication

After reading this you'll have a concrete plan for token-based auth on top of the existing Ktor client and `KeyValueStore`.

## Token storage

`KeyValueStore` (`shared/src/commonMain/kotlin/com/electra/template/data/storage/KeyValueStore.kt`) already wraps `multiplatform-settings` with typed accessors. For real apps move secrets to the Keychain / EncryptedSharedPreferences by substituting a platform-specific `Settings` implementation — the abstraction holds, only the underlying storage changes:

- Android: `androidx.security:security-crypto` + `EncryptedSharedPreferencesSettings`.
- iOS: a custom `Settings` backed by `SecItemAdd/Copy` or a library like `KeychainStore`.

Both plug into `AndroidSettingsFactory` / `IosSettingsFactory` — or add a second factory (e.g. `SecureSettingsFactory`) alongside the default so sensitive data lives in a separate store.

## Ktor Auth plugin

Install the plugin in `HttpClientFactory` (`shared/src/commonMain/kotlin/com/electra/template/data/network/HttpClientFactory.kt`):

```kotlin
install(Auth) {
    bearer {
        loadTokens { tokenProvider.current()?.let { BearerTokens(it.access, it.refresh) } }
        refreshTokens {
            val new = tokenProvider.refresh(oldTokens?.refreshToken ?: return@refreshTokens null)
            BearerTokens(new.access, new.refresh)
        }
    }
}
```

Inject the `TokenProvider` via Koin. Define the interface in `commonMain`:

```kotlin
interface TokenProvider {
    suspend fun current(): AuthTokens?
    suspend fun refresh(refreshToken: String): AuthTokens
}
```

The default impl reads/writes through `KeyValueStore` and calls a dedicated `AuthApi` for the refresh RPC.

## Expiring sessions

When the refresh endpoint returns 401, propagate an `AppException.Unauthorized` up from the Ktor error mapper (`ErrorInterceptor.kt` already has a case). Top-level ViewModels watch for that case and emit a `NavigateToLogin` side-effect — the same mechanism used today by `TodoListSideEffect.ShowError`.

## Bootstrap

Start the app on a splash route that calls `tokenProvider.current()`. If null, push a login destination; otherwise push the home. Add both to `core/navigation/Destination.kt` and make sure `DeepLinkParser` doesn't route unauthenticated users past login.
