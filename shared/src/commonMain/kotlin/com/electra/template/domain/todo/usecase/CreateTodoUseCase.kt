package com.electra.template.domain.todo.usecase

import com.electra.template.core.error.AppException
import com.electra.template.domain.todo.Todo
import com.electra.template.domain.todo.TodoRepository

class CreateTodoUseCase(private val repo: TodoRepository) {
    suspend operator fun invoke(title: String, description: String): Todo {
        if (title.isBlank()) throw AppException.Validation("title must not be blank")
        return repo.create(title.trim(), description.trim())
    }
}
