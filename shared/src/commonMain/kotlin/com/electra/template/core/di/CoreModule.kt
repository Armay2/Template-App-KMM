package com.electra.template.core.di

import com.electra.template.core.analytics.AnalyticsTracker
import com.electra.template.core.analytics.NoopAnalyticsTracker
import com.electra.template.core.coroutines.DefaultDispatcherProvider
import com.electra.template.core.coroutines.DispatcherProvider
import com.electra.template.core.crash.CrashReporter
import com.electra.template.core.crash.NoopCrashReporter
import com.electra.template.core.logging.KermitLogger
import com.electra.template.core.logging.Logger
import com.electra.template.core.theme.DesignTokens
import com.electra.template.data.storage.KeyValueStore
import com.russhwolf.settings.Settings
import org.koin.dsl.module

val coreModule =
    module {
        single<Logger> { KermitLogger() }
        single<AnalyticsTracker> { NoopAnalyticsTracker() }
        single<CrashReporter> { NoopCrashReporter() }
        single<DispatcherProvider> { DefaultDispatcherProvider() }
        single { DesignTokens.Default }
        single { KeyValueStore(get<Settings>()) }
    }
