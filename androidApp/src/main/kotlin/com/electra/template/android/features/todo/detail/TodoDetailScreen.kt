package com.electra.template.android.features.todo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.electra.template.presentation.todo.detail.TodoDetailSideEffect
import com.electra.template.presentation.todo.detail.TodoDetailViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TodoDetailScreen(
    id: String?,
    onDismiss: () -> Unit,
    vm: TodoDetailViewModel = koinViewModel(parameters = { parametersOf(id) }),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.effects.collect { e ->
            when (e) {
                TodoDetailSideEffect.Dismiss -> onDismiss()
                is TodoDetailSideEffect.ShowError -> { /* snackbar hook */ }
            }
        }
    }

    TodoDetailView(
        state = state,
        actions =
            TodoDetailActions(
                onTitleChange = vm::onTitleChange,
                onDescriptionChange = vm::onDescriptionChange,
                onToggle = vm::onToggle,
                onSave = vm::onSave,
                onDelete = vm::onDelete,
            ),
    )
}
