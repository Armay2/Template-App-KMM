package com.electra.template.android.features.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.electra.template.android.theme.AppTheme
import com.electra.template.domain.todo.Todo

@Composable
fun TodoRow(
    todo: Todo,
    onToggle: (String) -> Unit,
    onClick: (String) -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick(todo.id) }
            .padding(16.dp),
    ) {
        Checkbox(checked = todo.done, onCheckedChange = { onToggle(todo.id) })
        Text(todo.title, Modifier.padding(start = 16.dp))
    }
}

@Preview
@Composable
private fun TodoRowPreview() {
    AppTheme { TodoRow(Todo("1", "Buy milk", "", false), {}, {}) }
}
