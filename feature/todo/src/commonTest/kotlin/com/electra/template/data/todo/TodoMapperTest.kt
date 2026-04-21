package com.electra.template.data.todo

import com.electra.template.domain.todo.Todo
import kotlin.test.Test
import kotlin.test.assertEquals

class TodoMapperTest {
    @Test
    fun mapsDtoToDomain() {
        val dto = TodoDto("1", "t", "d", true)
        assertEquals(Todo("1", "t", "d", true), dto.toDomain())
    }

    @Test
    fun mapsDomainToDto() {
        val domain = Todo("2", "x", "y", false)
        assertEquals(TodoDto("2", "x", "y", false), domain.toDto())
    }
}
