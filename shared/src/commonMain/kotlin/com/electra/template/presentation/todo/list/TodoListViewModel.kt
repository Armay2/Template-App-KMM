package com.electra.template.presentation.todo.list

import com.electra.template.core.error.AppException
import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.GetTodosUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.base.BaseViewModel
import com.electra.template.presentation.base.UiStatus
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class TodoListViewModel(
    private val getTodos: GetTodosUseCase,
    private val toggle: ToggleTodoUseCase,
    private val create: CreateTodoUseCase,
    private val delete: DeleteTodoUseCase,
) : BaseViewModel<TodoListState, TodoListSideEffect>() {

    override val initialState = TodoListState()

    init {
        getTodos()
            .onEach { todos -> update { it.copy(todos = todos) } }
            .launchIn(scope)
        onRefresh()
    }

    fun onRefresh() {
        update { it.copy(status = UiStatus.Loading) }
        scope.launch {
            try {
                getTodos.refresh()
                update { it.copy(status = UiStatus.Idle) }
            } catch (e: AppException) {
                update { it.copy(status = UiStatus.Error(e.message ?: "error", retryable = true)) }
                emit(TodoListSideEffect.ShowError(e.message ?: "error"))
            }
        }
    }

    fun onSelect(id: String) { scope.launch { emit(TodoListSideEffect.NavigateToDetail(id)) } }
    fun onCreateNew() { scope.launch { emit(TodoListSideEffect.NavigateToDetail(null)) } }

    fun onToggle(id: String) {
        scope.launch {
            try { toggle(id) } catch (e: AppException) { emit(TodoListSideEffect.ShowError(e.message ?: "error")) }
        }
    }

    fun onDelete(id: String) {
        scope.launch {
            try { delete(id) } catch (e: AppException) { emit(TodoListSideEffect.ShowError(e.message ?: "error")) }
        }
    }
}
