package com.electra.template.android.navigation

import com.electra.template.core.navigation.Destination

object Routes {
    const val TodoList = "todos"
    const val TodoDetailPattern = "todos/{id}"
    fun todoDetail(id: String?) = "todos/${id ?: "new"}"
}

fun Destination.toRoute(): String = when (this) {
    is Destination.TodoList -> Routes.TodoList
    is Destination.TodoDetail -> Routes.todoDetail(id)
}
