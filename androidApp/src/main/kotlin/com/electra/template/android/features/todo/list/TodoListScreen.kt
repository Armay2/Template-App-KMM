package com.electra.template.android.features.todo.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.electra.template.core.navigation.Destination
import com.electra.template.presentation.todo.list.TodoListSideEffect
import com.electra.template.presentation.todo.list.TodoListViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun TodoListScreen(
    onNavigate: (Destination) -> Unit,
    vm: TodoListViewModel = koinViewModel(),
) {
    val state by vm.state.collectAsStateWithLifecycle()

    LaunchedEffect(vm) {
        vm.effects.collect { e ->
            when (e) {
                is TodoListSideEffect.NavigateToDetail -> onNavigate(Destination.TodoDetail(e.id))
                is TodoListSideEffect.ShowError -> { /* hook a SnackbarHostState here if desired */ }
            }
        }
    }

    TodoListView(
        state = state,
        actions =
            TodoListActions(
                onRefresh = vm::onRefresh,
                onCreate = vm::onCreateNew,
                onSelect = vm::onSelect,
                onToggle = vm::onToggle,
            ),
    )
}
