package com.electra.template.data.storage

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

actual class SettingsFactory {
    actual fun create(name: String): Settings =
        NSUserDefaultsSettings(NSUserDefaults(suiteName = name) ?: NSUserDefaults.standardUserDefaults)
}
