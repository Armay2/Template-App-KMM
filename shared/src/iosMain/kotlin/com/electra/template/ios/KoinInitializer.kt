package com.electra.template.ios

import com.electra.template.core.di.coreModule
import com.electra.template.core.di.platformModule
import com.electra.template.data.todo.todoModule
import org.koin.core.context.startKoin

class KoinInitializer {
    fun doInit() {
        startKoin {
            modules(coreModule, platformModule, todoModule)
        }
    }
}
