# Pagination

After reading this you'll know the recommended path for incremental list loading on each platform.

## Android — Paging 3

[Jetpack Paging 3](https://developer.android.com/jetpack/androidx/releases/paging) remains the default for Android list pagination. Add `androidx.paging:paging-compose` to `androidApp/build.gradle.kts` and expose a `PagingSource<Int, Todo>` (or any remote-mediator setup) on the ViewModel. Because Paging APIs are Android-only, keep the `PagingSource` in `androidApp/` and project the shared repository's data source into it — `TodoRepository` stays platform-agnostic.

## iOS — custom paginator in shared

`androidx.paging` has no iOS-native equivalent and isn't worth re-bridging. For cross-platform shops, ship a small paginator in shared state:

```kotlin
data class Page<T>(val items: List<T>, val nextCursor: String?)

class CursorPaginator<T>(
    initialCursor: String? = null,
    private val fetch: suspend (cursor: String?) -> Page<T>,
) {
    private val _items = MutableStateFlow<List<T>>(emptyList())
    val items: StateFlow<List<T>> = _items.asStateFlow()
    private var cursor: String? = initialCursor
    suspend fun loadMore() {
        val page = fetch(cursor); cursor = page.nextCursor
        _items.update { it + page.items }
    }
}
```

Call `loadMore()` from your ViewModel when the list reaches its threshold. On Android, use a `LazyListState.layoutInfo` observer; on iOS, use `.onAppear` on the last visible `ForEach` row. Both are cheap compared to adding a paging library.

## Infinite vs finite

For short lists (tens of items), don't paginate — let the refresh return everything. Pagination is an optimisation for data sets that do not fit in memory or whose upstream pages cost real money; don't add it speculatively.
