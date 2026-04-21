package com.electra.template.android.features.todo.list

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.electra.template.presentation.todo.list.TodoListFakes
import org.junit.Rule
import org.junit.Test

class TodoListViewTest {
    @get:Rule @JvmField
    val compose = createComposeRule()

    private val noOp = TodoListActions({}, {}, {}, {}, {}, {})

    @Test
    fun displaysItemsWhenStateHasTodos() {
        compose.setContent { TodoListView(TodoListFakes.WithItems, noOp) }
        compose.onNodeWithText("Buy groceries").assertIsDisplayed()
    }

    @Test
    fun displaysEmptyMessageWhenEmpty() {
        compose.setContent { TodoListView(TodoListFakes.Empty, noOp) }
        compose.onNodeWithText("Nothing to do.").assertIsDisplayed()
    }

    @Test
    fun doneItemsHiddenByDefault() {
        compose.setContent { TodoListView(TodoListFakes.WithItems, noOp) }
        compose.onNodeWithText("Buy groceries").assertIsDisplayed()
        compose.onNodeWithText("Walk the dog").assertIsNotDisplayed()
    }

    @Test
    fun expandedDoneSectionShowsDoneItems() {
        compose.setContent { TodoListView(TodoListFakes.WithItemsAndDone, noOp) }
        compose.onNodeWithText("Walk the dog").assertIsDisplayed()
        compose.onNodeWithText("Renew passport").assertIsDisplayed()
    }
}
