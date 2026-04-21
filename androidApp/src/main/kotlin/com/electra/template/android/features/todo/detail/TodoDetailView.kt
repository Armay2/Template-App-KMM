package com.electra.template.android.features.todo.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.electra.template.android.theme.AppTheme
import com.electra.template.presentation.todo.detail.TodoDetailFakes
import com.electra.template.presentation.todo.detail.TodoDetailState

data class TodoDetailActions(
    val onTitleChange: (String) -> Unit,
    val onDescriptionChange: (String) -> Unit,
    val onToggle: () -> Unit,
    val onSave: () -> Unit,
    val onDelete: () -> Unit,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailView(
    state: TodoDetailState,
    actions: TodoDetailActions,
) {
    Scaffold(topBar = { TopAppBar(title = { Text(if (state.id == null) "New todo" else "Edit todo") }) }) { padding ->
        Column(Modifier.fillMaxWidth().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = state.title,
                onValueChange = actions.onTitleChange,
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            )
            OutlinedTextField(
                value = state.description,
                onValueChange = actions.onDescriptionChange,
                label = { Text("Description") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
            Switch(state.done, onCheckedChange = { actions.onToggle() })
            Spacer(Modifier.height(16.dp))
            Button(actions.onSave) { Text("Save") }
            if (state.id != null) Button(actions.onDelete) { Text("Delete") }
        }
    }
}

@Preview @Composable
private fun NewPreview() = AppTheme { TodoDetailView(TodoDetailFakes.New, noOp()) }

@Preview @Composable
private fun ExistingPreview() = AppTheme { TodoDetailView(TodoDetailFakes.Existing, noOp()) }

private fun noOp() = TodoDetailActions({ _ -> }, { _ -> }, {}, {}, {})
