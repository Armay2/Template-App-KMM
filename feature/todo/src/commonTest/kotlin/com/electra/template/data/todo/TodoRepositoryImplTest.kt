package com.electra.template.data.todo

import app.cash.turbine.test
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TodoRepositoryImplTest {
    @Test
    fun refreshPopulatesTodos() =
        runTest {
            val repo = TodoRepositoryImpl(fakeTodoApi(listOf(TodoDto("1", "t", "d", false))))
            repo.refresh()
            assertEquals(1, repo.todos.value.size)
            assertEquals("t", repo.todos.value.first().title)
        }

    @Test
    fun toggleInvertsDoneInCache() =
        runTest {
            val repo = TodoRepositoryImpl(fakeTodoApi(listOf(TodoDto("1", "t", "d", false))))
            repo.refresh()
            repo.toggle("1")
            assertTrue(repo.todos.value.first().done)
        }

    @Test
    fun deleteRemovesFromCache() =
        runTest {
            val repo = TodoRepositoryImpl(fakeTodoApi(listOf(TodoDto("1", "t", "d", false))))
            repo.refresh()
            repo.todos.test {
                assertEquals(1, awaitItem().size)
                repo.delete("1")
                assertEquals(0, awaitItem().size)
            }
        }
}
