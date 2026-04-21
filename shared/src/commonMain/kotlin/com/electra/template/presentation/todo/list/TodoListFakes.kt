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
    val WithItemsAndDone =
        TodoListState(
            todos =
                listOf(
                    Todo("1", "Buy groceries", "Milk, bread, eggs", done = false),
                    Todo("2", "Book dentist", "", done = false),
                    Todo("3", "Draft Q3 OKRs", "", done = false),
                    Todo("4", "Walk the dog", "", done = true),
                    Todo("5", "Renew passport", "", done = true),
                ),
            isDoneExpanded = true,
            status = UiStatus.Idle,
        )
    val OnlyDone =
        TodoListState(
            todos =
                listOf(
                    Todo("1", "Walk the dog", "", done = true),
                    Todo("2", "Renew passport", "", done = true),
                    Todo("3", "Call mom", "", done = true),
                ),
            status = UiStatus.Idle,
        )
    val Error = TodoListState(status = UiStatus.Error("Network unavailable", retryable = true))
}
