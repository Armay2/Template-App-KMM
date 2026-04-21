package com.electra.template.domain.todo.usecase

import com.electra.template.domain.todo.Todo
import com.electra.template.domain.todo.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodosUseCase(private val repo: TodoRepository) {
    operator fun invoke(): Flow<List<Todo>> = repo.todos

    suspend fun refresh() = repo.refresh()
}
