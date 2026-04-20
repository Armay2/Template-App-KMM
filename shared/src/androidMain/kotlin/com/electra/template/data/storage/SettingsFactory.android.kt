package com.electra.template.data.storage

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

actual class SettingsFactory(private val context: Context) {
    actual fun create(name: String): Settings =
        SharedPreferencesSettings(context.getSharedPreferences(name, Context.MODE_PRIVATE))
}
