package com.electra.template.presentation.todo.list

import com.electra.template.domain.todo.Todo
import com.electra.template.presentation.base.UiStatus

object TodoListFakes {
    val Empty = TodoListState()
    val Loading = TodoListState(status = UiStatus.Loading)
    val WithItems =
        TodoListState(
            todos =
                listOf(
                    Todo("1", "Buy groceries", "Milk, bread, eggs", done = false),
                    Todo("2", "Walk the dog", "", done = true),
                ),
            status = UiStatus.Idle,
        )
    val Error = TodoListState(status = UiStatus.Error("Network unavailable", retryable = true))
}
