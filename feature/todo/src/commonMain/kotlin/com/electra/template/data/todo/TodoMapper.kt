package com.electra.template.data.todo

import com.electra.template.domain.todo.Todo

fun TodoDto.toDomain(): Todo = Todo(id, title, description, done)

fun Todo.toDto(): TodoDto = TodoDto(id, title, description, done)
