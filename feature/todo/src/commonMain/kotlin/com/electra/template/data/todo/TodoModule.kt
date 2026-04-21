package com.electra.template.data.todo

import com.electra.template.data.network.HttpClientFactory
import com.electra.template.domain.todo.TodoRepository
import com.electra.template.domain.todo.usecase.CreateTodoUseCase
import com.electra.template.domain.todo.usecase.DeleteTodoUseCase
import com.electra.template.domain.todo.usecase.GetTodosUseCase
import com.electra.template.domain.todo.usecase.ToggleTodoUseCase
import com.electra.template.presentation.todo.detail.TodoDetailViewModel
import com.electra.template.presentation.todo.list.TodoListViewModel
import io.ktor.client.engine.HttpClientEngine
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val todoModule =
    module {
        single(named("todoBaseUrl")) { "https://jsonplaceholder.typicode.com" }
        single { HttpClientFactory.create(get<HttpClientEngine>()) }
        single { TodoApi(get(), get(named("todoBaseUrl"))) }
        single<TodoRepository> { TodoRepositoryImpl(get()) }

        factory { GetTodosUseCase(get()) }
        factory { ToggleTodoUseCase(get()) }
        factory { CreateTodoUseCase(get()) }
        factory { DeleteTodoUseCase(get()) }

        viewModel { TodoListViewModel(get(), get(), get(), get()) }
        viewModel { (id: String?) -> TodoDetailViewModel(id, get(), get(), get(), get()) }
    }
