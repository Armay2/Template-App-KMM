package com.electra.template.domain.todo

import com.electra.template.core.error.AppException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    val todos: Flow<List<Todo>>

    @Throws(AppException::class, CancellationException::class)
    suspend fun refresh()

    @Throws(AppException::class, CancellationException::class)
    suspend fun get(id: String): Todo

    @Throws(AppException::class, CancellationException::class)
    suspend fun toggle(id: String)

    @Throws(AppException::class, CancellationException::class)
    suspend fun create(
        title: String,
        description: String,
    ): Todo

    @Throws(AppException::class, CancellationException::class)
    suspend fun delete(id: String)
}
