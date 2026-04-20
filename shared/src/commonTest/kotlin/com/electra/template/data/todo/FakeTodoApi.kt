package com.electra.template.data.todo

import com.electra.template.data.network.HttpClientFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.http.headersOf
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

fun fakeTodoApi(initial: List<TodoDto>): TodoApi {
    val json = Json { ignoreUnknownKeys = true }
    val store = initial.associateBy { it.id }.toMutableMap()
    val engine = MockEngine { req ->
        val path = req.url.encodedPath.trimEnd('/').removePrefix("/api")
        val method = req.method.value
        val headers = headersOf(HttpHeaders.ContentType, "application/json")

        suspend fun readBody(): String =
            (req.body as? OutgoingContent.ByteArrayContent)?.bytes()?.decodeToString() ?: "{}"

        when {
            method == "GET" && path == "/todos" -> respond(
                json.encodeToString(ListSerializer(TodoDto.serializer()), store.values.toList()),
                HttpStatusCode.OK,
                headers,
            )
            method == "GET" && path.startsWith("/todos/") -> {
                val id = path.removePrefix("/todos/")
                val item = store[id]
                if (item != null) {
                    respond(json.encodeToString(TodoDto.serializer(), item), HttpStatusCode.OK, headers)
                } else {
                    respond("not found", HttpStatusCode.NotFound)
                }
            }
            method == "POST" && path == "/todos" -> {
                val dto = json.decodeFromString(TodoDto.serializer(), readBody())
                val created = dto.copy(id = (store.size + 1).toString())
                store[created.id] = created
                respond(json.encodeToString(TodoDto.serializer(), created), HttpStatusCode.Created, headers)
            }
            method == "PUT" && path.startsWith("/todos/") -> {
                val dto = json.decodeFromString(TodoDto.serializer(), readBody())
                store[dto.id] = dto
                respond(json.encodeToString(TodoDto.serializer(), dto), HttpStatusCode.OK, headers)
            }
            method == "DELETE" && path.startsWith("/todos/") -> {
                val id = path.removePrefix("/todos/")
                store.remove(id)
                respond("", HttpStatusCode.NoContent)
            }
            else -> respond("not implemented: $method $path", HttpStatusCode.NotImplemented)
        }
    }
    return TodoApi(HttpClientFactory.create(engine), baseUrl = "/api")
}
