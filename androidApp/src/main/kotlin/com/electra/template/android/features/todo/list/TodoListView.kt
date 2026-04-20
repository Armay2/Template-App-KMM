package com.electra.template.android.features.todo.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.electra.template.android.features.todo.components.TodoRow
import com.electra.template.android.theme.AppTheme
import com.electra.template.presentation.base.UiStatus
import com.electra.template.presentation.todo.list.TodoListFakes
import com.electra.template.presentation.todo.list.TodoListState

data class TodoListActions(
    val onRefresh: () -> Unit,
    val onCreate: () -> Unit,
    val onSelect: (String) -> Unit,
    val onToggle: (String) -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListView(state: TodoListState, actions: TodoListActions) {
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
                state.todos.isEmpty() -> Text("No todos yet.")
                else -> LazyColumn(Modifier.fillMaxSize()) {
                    items(state.todos, key = { it.id }) { t ->
                        TodoRow(t, onToggle = actions.onToggle, onClick = actions.onSelect)
                    }
                }
            }
        }
    }
}

@Preview @Composable private fun EmptyPreview() =
    AppTheme { TodoListView(TodoListFakes.Empty, noOpActions()) }
@Preview @Composable private fun LoadingPreview() =
    AppTheme { TodoListView(TodoListFakes.Loading, noOpActions()) }
@Preview @Composable private fun ItemsPreview() =
    AppTheme { TodoListView(TodoListFakes.WithItems, noOpActions()) }

private fun noOpActions() = TodoListActions({}, {}, {}, {})
