package com.electra.template.android.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.electra.template.android.features.todo.detail.TodoDetailScreen
import com.electra.template.android.features.todo.list.TodoListScreen

@Composable
fun AppNavHost() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = Routes.TodoList) {
        composable(Routes.TodoList) {
            TodoListScreen(onNavigate = { dest ->
                nav.navigate(dest.toRoute())
            })
        }
        composable(
            route = Routes.TodoDetailPattern,
            arguments =
                listOf(
                    navArgument("id") {
                        type = NavType.StringType
                        nullable = false
                    },
                ),
        ) { backStack ->
            val raw = backStack.arguments?.getString("id")
            val id = raw?.takeIf { it != "new" }
            TodoDetailScreen(
                id = id,
                onDismiss = { nav.popBackStack() },
            )
        }
    }
}
