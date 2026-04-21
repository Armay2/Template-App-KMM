package com.electra.template.ios

import com.electra.template.presentation.todo.detail.TodoDetailViewModel
import com.electra.template.presentation.todo.list.TodoListViewModel
import org.koin.core.parameter.parametersOf
import org.koin.mp.KoinPlatform

object KoinBridge {
    fun todoListViewModel(): TodoListViewModel = KoinPlatform.getKoin().get()
    fun todoDetailViewModel(id: String?): TodoDetailViewModel =
        KoinPlatform.getKoin().get { parametersOf(id) }
}
