package com.electra.template.data.storage

import com.russhwolf.settings.NSUserDefaultsSettings
import com.russhwolf.settings.Settings
import platform.Foundation.NSUserDefaults

class IosSettingsFactory : SettingsFactory {
    override fun create(name: String): Settings =
        NSUserDefaultsSettings(NSUserDefaults(suiteName = name) ?: NSUserDefaults.standardUserDefaults)
}
