package com.electra.template.presentation.todo.list

import com.electra.template.domain.todo.Todo
import com.electra.template.presentation.base.UiStatus

data class TodoListState(
    val todos: List<Todo> = emptyList(),
    val status: UiStatus = UiStatus.Idle,
)
