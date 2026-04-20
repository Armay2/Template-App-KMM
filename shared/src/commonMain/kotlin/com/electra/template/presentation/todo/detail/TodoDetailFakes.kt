package com.electra.template.presentation.todo.detail

object TodoDetailFakes {
    val New = TodoDetailState()
    val Existing = TodoDetailState(
        id = "1",
        title = "Buy groceries",
        description = "Milk, bread, eggs",
        done = false,
    )
}
