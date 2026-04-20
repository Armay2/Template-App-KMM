package com.electra.template.core.navigation

sealed interface Destination {
    data object TodoList : Destination
    data class TodoDetail(val id: String?) : Destination // id == null means "create new"
}
