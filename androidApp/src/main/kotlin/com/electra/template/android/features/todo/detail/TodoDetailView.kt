package com.electra.template.android.features.todo.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(if (state.id == null) "New todo" else "Edit todo") }) },
    ) { padding ->
        TodoDetailForm(
            state = state,
            actions = actions,
            onDeleteClick = { showDeleteConfirm = true },
            contentPadding = padding,
        )
    }

    if (showDeleteConfirm) {
        DeleteConfirmDialog(
            onConfirm = {
                showDeleteConfirm = false
                actions.onDelete()
            },
            onDismiss = { showDeleteConfirm = false },
        )
    }
}

@Composable
private fun TodoDetailForm(
    state: TodoDetailState,
    actions: TodoDetailActions,
    onDeleteClick: () -> Unit,
    contentPadding: PaddingValues,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        OutlinedTextField(
            value = state.title,
            onValueChange = actions.onTitleChange,
            label = { Text("Title") },
            textStyle = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.fillMaxWidth(),
        )
        OutlinedTextField(
            value = state.description,
            onValueChange = actions.onDescriptionChange,
            label = { Text("Description") },
            minLines = 4,
            maxLines = 10,
            modifier = Modifier.fillMaxWidth(),
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = "Completed",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.weight(1f),
            )
            Switch(checked = state.done, onCheckedChange = { actions.onToggle() })
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = actions.onSave,
            modifier = Modifier.fillMaxWidth(),
        ) { Text("Save") }
        if (state.id != null) {
            OutlinedButton(
                onClick = onDeleteClick,
                colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error,
                    ),
                modifier = Modifier.fillMaxWidth(),
            ) { Text("Delete") }
        }
    }
}

@Composable
private fun DeleteConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete this todo?") },
        text = { Text("This action cannot be undone.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Delete", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Preview @Composable
private fun NewPreview() = AppTheme { TodoDetailView(TodoDetailFakes.New, noOp()) }

@Preview @Composable
private fun ExistingPreview() = AppTheme { TodoDetailView(TodoDetailFakes.Existing, noOp()) }

private fun noOp() = TodoDetailActions({}, {}, {}, {}, {})
