package com.electra.template.data.storage

import com.russhwolf.settings.Settings

interface SettingsFactory {
    fun create(name: String = "app"): Settings
}
