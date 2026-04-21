package com.electra.template.core.di

import android.content.Context
import com.electra.template.data.storage.AndroidSettingsFactory
import com.electra.template.data.storage.SettingsFactory
import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

val androidPlatformModule =
    module {
        single<SettingsFactory> { AndroidSettingsFactory(get<Context>()) }
        single<Settings> { get<SettingsFactory>().create() }
        single<HttpClientEngine> { OkHttp.create() }
    }
