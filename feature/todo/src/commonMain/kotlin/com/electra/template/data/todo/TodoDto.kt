package com.electra.template.data.todo

import kotlinx.serialization.Serializable

@Serializable
data class TodoDto(
    val id: String,
    val title: String,
    val description: String = "",
    val done: Boolean = false,
)
