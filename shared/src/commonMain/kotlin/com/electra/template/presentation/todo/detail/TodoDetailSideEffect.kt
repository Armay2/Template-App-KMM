package com.electra.template.presentation.todo.detail

sealed interface TodoDetailSideEffect {
    data object Dismiss : TodoDetailSideEffect

    data class ShowError(val message: String) : TodoDetailSideEffect
}
