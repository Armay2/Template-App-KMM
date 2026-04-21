package com.electra.template.android.features.todo.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.electra.template.android.theme.AppTheme
import com.electra.template.domain.todo.Todo

private const val DONE_ROW_ALPHA = 0.5f
private const val ACTIVE_ROW_ALPHA = 1f

@Composable
fun TodoRow(
    todo: Todo,
    onToggle: (String) -> Unit,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val haptics = LocalHapticFeedback.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier =
            modifier
                .fillMaxWidth()
                .clickable { onClick(todo.id) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .alpha(if (todo.done) DONE_ROW_ALPHA else ACTIVE_ROW_ALPHA),
    ) {
        Checkbox(
            checked = todo.done,
            onCheckedChange = {
                haptics.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                onToggle(todo.id)
            },
        )
        Text(
            text = todo.title,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (todo.done) TextDecoration.LineThrough else TextDecoration.None,
            modifier = Modifier.padding(start = 12.dp),
        )
    }
}

@Preview @Composable
private fun ActiveRowPreview() = AppTheme { TodoRow(Todo("1", "Buy milk", "", false), {}, {}) }

@Preview @Composable
private fun DoneRowPreview() = AppTheme { TodoRow(Todo("2", "Walk the dog", "", true), {}, {}) }
