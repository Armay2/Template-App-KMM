package com.electra.template.android.features.todo.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.electra.template.android.features.todo.components.TodoRow
import com.electra.template.android.theme.AppTheme
import com.electra.template.domain.todo.Todo
import com.electra.template.presentation.base.UiStatus
import com.electra.template.presentation.todo.list.TodoListFakes
import com.electra.template.presentation.todo.list.TodoListState

data class TodoListActions(
    val onRefresh: () -> Unit,
    val onCreate: () -> Unit,
    val onSelect: (String) -> Unit,
    val onToggle: (String) -> Unit,
    val onDelete: (String) -> Unit,
    val onToggleDoneSection: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListView(
    state: TodoListState,
    actions: TodoListActions,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Todos") }) },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = actions.onCreate,
                icon = { Icon(Icons.Default.Add, null) },
                text = { Text("New") },
            )
        },
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
            when {
                state.status is UiStatus.Loading && state.todos.isEmpty() -> CircularProgressIndicator()
                state.todos.isEmpty() -> TodoEmptyState()
                state.active.isEmpty() ->
                    AllCaughtUpView(
                        doneCount = state.done.size,
                        isDoneExpanded = state.isDoneExpanded,
                        done = state.done,
                        actions = actions,
                    )
                else ->
                    LazyColumn(Modifier.fillMaxSize()) {
                        items(state.active, key = { "a-${it.id}" }) { t ->
                            SwipeableRow(t, actions, modifier = Modifier.animateItem())
                        }
                        if (state.done.isNotEmpty()) {
                            item(key = "done-header") {
                                TodoSectionHeader(
                                    title = "Done",
                                    count = state.done.size,
                                    expanded = state.isDoneExpanded,
                                    onToggle = actions.onToggleDoneSection,
                                    modifier = Modifier.animateItem(),
                                )
                            }
                            if (state.isDoneExpanded) {
                                items(state.done, key = { "d-${it.id}" }) { t ->
                                    SwipeableRow(t, actions, modifier = Modifier.animateItem())
                                }
                            }
                        }
                    }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableRow(
    todo: Todo,
    actions: TodoListActions,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    val dismissState =
        rememberSwipeToDismissBoxState(
            positionalThreshold = { it * 0.6f },
        )
    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            haptics.performHapticFeedback(HapticFeedbackType.LongPress)
            actions.onDelete(todo.id)
        }
    }
    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd,
            ) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                )
            }
        },
        modifier = modifier,
    ) {
        TodoRow(todo = todo, onToggle = actions.onToggle, onClick = actions.onSelect)
    }
}

@Composable
private fun AllCaughtUpView(
    doneCount: Int,
    isDoneExpanded: Boolean,
    done: List<Todo>,
    actions: TodoListActions,
) {
    LazyColumn(Modifier.fillMaxSize()) {
        item(key = "caught-up") {
            Box(Modifier.fillMaxSize().padding(24.dp), contentAlignment = Alignment.Center) {
                Text("All caught up! 🎉", style = MaterialTheme.typography.titleMedium)
            }
        }
        item(key = "done-header") {
            TodoSectionHeader(
                title = "Done",
                count = doneCount,
                expanded = isDoneExpanded,
                onToggle = actions.onToggleDoneSection,
            )
        }
        if (isDoneExpanded) {
            items(done, key = { "d-${it.id}" }) { t ->
                SwipeableRow(t, actions)
            }
        }
    }
}

private fun noOpActions() = TodoListActions({}, {}, {}, {}, {}, {})

@Preview @Composable
private fun EmptyPreview() = AppTheme { TodoListView(TodoListFakes.Empty, noOpActions()) }

@Preview @Composable
private fun LoadingPreview() = AppTheme { TodoListView(TodoListFakes.Loading, noOpActions()) }

@Preview @Composable
private fun ItemsPreview() = AppTheme { TodoListView(TodoListFakes.WithItems, noOpActions()) }

@Preview @Composable
private fun WithDonePreview() = AppTheme { TodoListView(TodoListFakes.WithItemsAndDone, noOpActions()) }

@Preview @Composable
private fun AllCaughtUpPreview() = AppTheme { TodoListView(TodoListFakes.OnlyDone, noOpActions()) }
