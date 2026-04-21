package com.electra.template.presentation.todo.detail

import com.electra.template.core.error.AppException
import com.electra.template.domain.todo.TodoRepository
import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.base.BaseViewModel
import com.electra.template.presentation.base.UiStatus
import kotlinx.coroutines.launch

class TodoDetailViewModel(
    private val id: String?,
    private val repository: TodoRepository,
    private val toggle: ToggleTodoUseCase,
    private val create: CreateTodoUseCase,
    private val delete: DeleteTodoUseCase,
) : BaseViewModel<TodoDetailState, TodoDetailSideEffect>() {
    override val initialState = TodoDetailState(id = id)

    init {
        if (id != null) load()
    }

    private fun load() {
        update { it.copy(status = UiStatus.Loading) }
        scope.launch {
            try {
                val t = repository.get(id!!)
                update { it.copy(title = t.title, description = t.description, done = t.done, status = UiStatus.Idle) }
            } catch (e: AppException) {
                update { it.copy(status = UiStatus.Error(e.message ?: "error", retryable = true)) }
            }
        }
    }

    fun onTitleChange(v: String) = update { it.copy(title = v) }

    fun onDescriptionChange(v: String) = update { it.copy(description = v) }

    fun onToggle() {
        val current = state.value.id ?: return
        scope.launch {
            try {
                toggle(current)
                update { it.copy(done = !it.done) }
            } catch (
                e: AppException,
            ) {
                emit(TodoDetailSideEffect.ShowError(e.message ?: "error"))
            }
        }
    }

    fun onSave() {
        scope.launch {
            try {
                if (state.value.id == null) {
                    create(state.value.title, state.value.description)
                }
                emit(TodoDetailSideEffect.Dismiss)
            } catch (e: AppException) {
                update { it.copy(status = UiStatus.Error(e.message ?: "error", retryable = false)) }
            }
        }
    }

    fun onDelete() {
        val current = state.value.id ?: return
        scope.launch {
            try {
                delete(current)
                emit(TodoDetailSideEffect.Dismiss)
            } catch (
                e: AppException,
            ) {
                emit(TodoDetailSideEffect.ShowError(e.message ?: "error"))
            }
        }
    }
}
