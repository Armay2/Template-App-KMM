package com.electra.template.data.storage

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

class AndroidSettingsFactory(private val context: Context) : SettingsFactory {
    override fun create(name: String): Settings = SharedPreferencesSettings(context.getSharedPreferences(name, Context.MODE_PRIVATE))
}
