package com.electra.template.presentation.todo.detail

import com.electra.template.domain.todo.TodoRepository
import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.base.BaseViewModel

// TEMPORARY STUB - filled in by Task 2.4
class TodoDetailViewModel(
    id: String?,
    repository: TodoRepository,
    toggle: ToggleTodoUseCase,
    create: CreateTodoUseCase,
    delete: DeleteTodoUseCase,
) : BaseViewModel<Unit, Unit>() {
    override val initialState: Unit = Unit
}
