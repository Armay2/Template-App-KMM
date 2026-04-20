package com.electra.template.data.storage

import com.russhwolf.settings.Settings

expect class SettingsFactory {
    fun create(name: String = "app"): Settings
}
