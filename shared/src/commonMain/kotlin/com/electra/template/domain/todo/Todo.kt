package com.electra.template.domain.todo

data class Todo(
    val id: String,
    val title: String,
    val description: String,
    val done: Boolean,
)
