package com.electra.template.data.todo

import com.electra.template.data.network.toAppException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class TodoApi(private val client: HttpClient, private val baseUrl: String) {
    suspend fun list(): List<TodoDto> = wrap { client.get("$baseUrl/todos").body() }
    suspend fun get(id: String): TodoDto = wrap { client.get("$baseUrl/todos/$id").body() }
    suspend fun create(dto: TodoDto): TodoDto = wrap {
        client.post("$baseUrl/todos") { contentType(ContentType.Application.Json); setBody(dto) }.body()
    }
    suspend fun update(dto: TodoDto): TodoDto = wrap {
        client.put("$baseUrl/todos/${dto.id}") { contentType(ContentType.Application.Json); setBody(dto) }.body()
    }
    suspend fun delete(id: String) { wrap { client.delete("$baseUrl/todos/$id").body<Unit>() } }

    private suspend inline fun <T> wrap(block: () -> T): T = try { block() } catch (t: Throwable) { throw t.toAppException() }
}
