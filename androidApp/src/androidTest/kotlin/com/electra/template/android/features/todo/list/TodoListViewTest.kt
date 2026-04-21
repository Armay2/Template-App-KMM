package com.electra.template.android.features.todo.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.electra.template.presentation.todo.list.TodoListFakes
import org.junit.Rule
import org.junit.Test

class TodoListViewTest {
    @Rule @JvmField
    val compose = createComposeRule()

    @Test
    fun displaysItemsWhenStateHasTodos() {
        compose.setContent {
            TodoListView(TodoListFakes.WithItems, TodoListActions({}, {}, {}, {}))
        }
        compose.onNodeWithText("Buy groceries").assertIsDisplayed()
        compose.onNodeWithText("Walk the dog").assertIsDisplayed()
    }

    @Test
    fun displaysEmptyMessageWhenEmpty() {
        compose.setContent {
            TodoListView(TodoListFakes.Empty, TodoListActions({}, {}, {}, {}))
        }
        compose.onNodeWithText("No todos yet.").assertIsDisplayed()
    }
}
