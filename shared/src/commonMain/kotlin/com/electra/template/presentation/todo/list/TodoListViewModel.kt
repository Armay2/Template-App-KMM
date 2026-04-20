package com.electra.template.presentation.todo.list

import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.GetTodosUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.base.BaseViewModel

// TEMPORARY STUB - filled in by Task 2.4
class TodoListViewModel(
    getTodos: GetTodosUseCase,
    toggle: ToggleTodoUseCase,
    create: CreateTodoUseCase,
    delete: DeleteTodoUseCase,
) : BaseViewModel<Unit, Unit>() {
    override val initialState: Unit = Unit
}
