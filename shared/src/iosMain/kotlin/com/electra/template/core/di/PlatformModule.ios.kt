package com.electra.template.core.di

import com.electra.template.data.storage.SettingsFactory
import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual val platformModule = module {
    single { SettingsFactory() }
    single<Settings> { get<SettingsFactory>().create() }
    single<HttpClientEngine> { Darwin.create() }
}
