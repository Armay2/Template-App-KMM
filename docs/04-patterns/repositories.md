# Repositories

After reading this you'll know the in-memory-cache pattern and how DTOs are kept out of the domain layer.

## Shape

Every repository is an interface in `domain/<feature>/` and an impl in `data/<feature>/`. Public methods that can fail are annotated for Swift interop:

```kotlin
interface TodoRepository {
    val todos: Flow<List<Todo>>
    @Throws(AppException::class, CancellationException::class) suspend fun refresh()
    @Throws(AppException::class, CancellationException::class) suspend fun get(id: String): Todo
    // ...
}
```

The implementation wraps remote + cache (`shared/src/commonMain/kotlin/com/electra/template/data/todo/TodoRepositoryImpl.kt`):

```kotlin
class TodoRepositoryImpl(private val api: TodoApi) : TodoRepository {
    private val cache = MutableStateFlow<List<Todo>>(emptyList())
    override val todos: StateFlow<List<Todo>> = cache.asStateFlow()

    override suspend fun refresh() { cache.value = api.list().map { it.toDomain() } }

    override suspend fun toggle(id: String) {
        val current = cache.value.firstOrNull { it.id == id } ?: api.get(id).toDomain()
        val updated = api.update(current.copy(done = !current.done).toDto()).toDomain()
        cache.update { list -> list.map { if (it.id == id) updated else it } }
    }
    // ...
}
```

## DTO ↔ domain

DTOs live in `data/<feature>/` with `@Serializable` (kotlinx-serialization). They only exist to model the wire format and should never leak out of the data layer. Mapping is one file — `TodoMapper.kt`:

```kotlin
fun TodoDto.toDomain(): Todo = Todo(id, title, description, done)
fun Todo.toDto(): TodoDto = TodoDto(id, title, description, done)
```

## Error discipline

All network errors are translated in `TodoApi.wrap { ... }.toAppException()` (`data/network/ErrorInterceptor.kt`) so repositories only ever throw `AppException` subtypes. `CancellationException` rethrows from `ErrorMapper.map` — never swallow it, or coroutine cancellation breaks.
