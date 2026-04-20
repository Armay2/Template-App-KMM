package com.electra.template.core.navigation

object DeepLinkParser {
    private const val SCHEME = "template://"
    fun parse(url: String): Destination? {
        if (!url.startsWith(SCHEME)) return null
        val path = url.removePrefix(SCHEME).trim('/').split('/')
        return when {
            path == listOf("todos") -> Destination.TodoList
            path.size == 2 && path[0] == "todos" && path[1] == "new" -> Destination.TodoDetail(null)
            path.size == 2 && path[0] == "todos" -> Destination.TodoDetail(path[1])
            else -> null
        }
    }
}
