package com.electra.template.domain.todo.usecase

import com.electra.template.domain.todo.TodoRepository

class DeleteTodoUseCase(private val repo: TodoRepository) {
    suspend operator fun invoke(id: String) = repo.delete(id)
}
