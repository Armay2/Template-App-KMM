package com.electra.template.presentation.todo.list

import app.cash.turbine.test
import com.electra.template.data.todo.TodoDto
import com.electra.template.data.todo.TodoRepositoryImpl
import com.electra.template.data.todo.fakeTodoApi
import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.GetTodosUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.base.UiStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

@OptIn(ExperimentalCoroutinesApi::class)
class TodoListViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setMain() {
        Dispatchers.setMain(dispatcher)
    }

    @AfterTest fun resetMain() {
        Dispatchers.resetMain()
    }

    private fun vm(initial: List<TodoDto>): TodoListViewModel {
        val repo = TodoRepositoryImpl(fakeTodoApi(initial))
        return TodoListViewModel(
            getTodos = GetTodosUseCase(repo),
            toggle = ToggleTodoUseCase(repo),
            create = CreateTodoUseCase(repo),
            delete = DeleteTodoUseCase(repo),
        )
    }

    @Test
    fun loadsTodosOnRefresh() =
        runTest(dispatcher) {
            val vm = vm(listOf(TodoDto("1", "a", "", false)))
            vm.onRefresh()
            dispatcher.scheduler.advanceUntilIdle()
            assertEquals(1, vm.state.value.todos.size)
            assertIs<UiStatus.Idle>(vm.state.value.status)
        }

    @Test
    fun emitsNavigateToDetailOnSelect() =
        runTest(dispatcher) {
            val vm = vm(emptyList())
            vm.effects.test {
                vm.onSelect("42")
                assertEquals(TodoListSideEffect.NavigateToDetail("42"), awaitItem())
            }
        }
}
