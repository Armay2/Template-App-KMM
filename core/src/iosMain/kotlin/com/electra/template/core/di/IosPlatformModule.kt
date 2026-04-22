package com.electra.template.core.di

import com.electra.template.data.storage.IosSettingsFactory
import com.electra.template.data.storage.SettingsFactory
import com.russhwolf.settings.Settings
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

val iosPlatformModule =
    module {
        single<SettingsFactory> { IosSettingsFactory() }
        single<Settings> { get<SettingsFactory>().create() }
        single<HttpClientEngine> { Darwin.create() }
    }
