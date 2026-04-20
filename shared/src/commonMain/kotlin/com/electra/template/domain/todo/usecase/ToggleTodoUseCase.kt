package com.electra.template.domain.todo.usecase

import com.electra.template.domain.todo.TodoRepository

class ToggleTodoUseCase(private val repo: TodoRepository) {
    suspend operator fun invoke(id: String) = repo.toggle(id)
}
