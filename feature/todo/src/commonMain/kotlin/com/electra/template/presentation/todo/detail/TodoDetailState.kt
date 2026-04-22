package com.electra.template.presentation.todo.detail

import com.electra.template.presentation.base.UiStatus

data class TodoDetailState(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val done: Boolean = false,
    val status: UiStatus = UiStatus.Idle,
)
