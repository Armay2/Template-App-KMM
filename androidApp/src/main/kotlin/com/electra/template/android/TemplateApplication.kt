package com.electra.template.android

import android.app.Application
import com.electra.template.android.di.androidModule
import com.electra.template.core.di.coreModule
import com.electra.template.core.di.platformModule
import com.electra.template.data.todo.todoModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TemplateApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TemplateApplication)
            modules(coreModule, platformModule, todoModule, androidModule)
        }
    }
}
