package com.electra.template.presentation.todo.list

sealed interface TodoListSideEffect {
    data class NavigateToDetail(val id: String?) : TodoListSideEffect
    data class ShowError(val message: String) : TodoListSideEffect
}
