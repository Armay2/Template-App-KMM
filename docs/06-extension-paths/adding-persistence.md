# Adding persistence (SQLDelight or Room KMP)

After reading this you'll know which local-DB options fit the template, what the `multiplatform-settings` layer already covers, and the concrete steps to add a schema.

## What's already there

`shared/src/commonMain/kotlin/com/electra/template/data/storage/KeyValueStore.kt` wraps `multiplatform-settings` (`com.russhwolf:multiplatform-settings`) for key-value needs: feature flags, selected theme, auth tokens, small user preferences. `SettingsFactory` is an `expect` with `SharedPreferencesSettings` on Android and `NSUserDefaultsSettings` on iOS.

This covers "a few scalars per user", not "the app's main data store". For lists, joins, sync queues, or anything relational, add a real database.

## Preferred: SQLDelight

[SQLDelight](https://cashapp.github.io/sqldelight/) generates type-safe Kotlin from `.sq` SQL files and runs on both platforms.

1. Add the plugin + driver deps in `shared/build.gradle.kts`:
   ```kotlin
   plugins { alias(libs.plugins.sqldelight) }
   // commonMain: app.cash.sqldelight:coroutines-extensions
   // androidMain: app.cash.sqldelight:android-driver
   // iosMain:     app.cash.sqldelight:native-driver
   ```
   Register the new version in `gradle/libs.versions.toml`.
2. Put `.sq` files under `shared/src/commonMain/sqldelight/<package>/`.
3. Provide the driver through `expect class DatabaseDriverFactory` (Android: `AndroidSqliteDriver(schema, context, "app.db")`, iOS: `NativeSqliteDriver(schema, "app.db")`) exactly like `SettingsFactory` today.
4. Bind `AppDatabase(get())` as a `single` in `coreModule` and replace `TodoRepositoryImpl`'s `MutableStateFlow` cache with a query `asFlow()`.

## Alternative: Room KMP

Room has preview-quality KMP support now. It's a better fit if you have a strong Android-side bias in your team or already run migrations on SQLite via Room. Integration steps mirror SQLDelight; Room needs KSP (already configured — see `libs.plugins.ksp` in `shared/build.gradle.kts`) and an `expect` factory to hand Room the correct builder per platform.

## Where to slot it

- Keep the repository interface in `domain/<feature>/` unchanged. Only `data/<feature>/<Feature>RepositoryImpl.kt` is affected.
- Expose queries as `Flow<List<Entity>>` — the ViewModel already collects them the same way it collects `MutableStateFlow` today.
- Add a new migration directory under `shared/src/commonMain/sqldelight/migrations/` (SQLDelight) or a `Migration` class (Room).

This is the single largest deviation from the template's bundled data path — plan ~1 day for integration + tests.
