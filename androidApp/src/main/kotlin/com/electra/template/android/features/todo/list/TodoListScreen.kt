package com.electra.template.android.features.todo.list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    var showQuickAdd by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(vm) {
        vm.effects.collect { e ->
            when (e) {
                is TodoListSideEffect.NavigateToDetail -> onNavigate(Destination.TodoDetail(e.id))
                TodoListSideEffect.OpenQuickAdd -> showQuickAdd = true
                is TodoListSideEffect.ShowError -> Unit
            }
        }
    }

    TodoListView(
        state = state,
        actions =
            TodoListActions(
                onRefresh = vm::onRefresh,
                onCreate = vm::onRequestQuickAdd,
                onSelect = vm::onSelect,
                onToggle = vm::onToggle,
                onDelete = vm::onDelete,
                onToggleDoneSection = vm::onToggleDoneSection,
            ),
    )

    if (showQuickAdd) {
        QuickAddSheet(
            onDismiss = { showQuickAdd = false },
            onCreate = { title -> vm.onQuickAdd(title) },
        )
    }
}
