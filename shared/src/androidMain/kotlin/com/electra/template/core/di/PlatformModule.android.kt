package com.electra.template.core.di

import android.content.Context
import com.electra.template.data.storage.SettingsFactory
import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual val platformModule =
    module {
        single { SettingsFactory(get<Context>()) }
        single<Settings> { get<SettingsFactory>().create() }
        single<HttpClientEngine> { OkHttp.create() }
    }
