package com.electra.template.data.todo

import com.electra.template.core.error.AppException
import com.electra.template.domain.todo.Todo
import com.electra.template.domain.todo.TodoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TodoRepositoryImpl(private val api: TodoApi) : TodoRepository {
    private val cache = MutableStateFlow<List<Todo>>(emptyList())
    override val todos: StateFlow<List<Todo>> = cache.asStateFlow()

    @Throws(AppException::class, CancellationException::class)
    override suspend fun refresh() {
        cache.value = api.list().map { it.toDomain() }
    }

    @Throws(AppException::class, CancellationException::class)
    override suspend fun get(id: String): Todo = cache.value.firstOrNull { it.id == id } ?: api.get(id).toDomain()

    @Throws(AppException::class, CancellationException::class)
    override suspend fun toggle(id: String) {
        val current = cache.value.firstOrNull { it.id == id } ?: api.get(id).toDomain()
        val updated = api.update(current.copy(done = !current.done).toDto()).toDomain()
        cache.update { list -> list.map { if (it.id == id) updated else it } }
    }

    @Throws(AppException::class, CancellationException::class)
    override suspend fun create(
        title: String,
        description: String,
    ): Todo {
        val dto = api.create(TodoDto(id = "", title = title, description = description, done = false))
        val created = dto.toDomain()
        cache.update { it + created }
        return created
    }

    @Throws(AppException::class, CancellationException::class)
    override suspend fun delete(id: String) {
        api.delete(id)
        cache.update { list -> list.filterNot { it.id == id } }
    }
}
