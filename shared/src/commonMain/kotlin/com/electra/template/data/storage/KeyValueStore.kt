package com.electra.template.data.storage

import com.russhwolf.settings.Settings

class KeyValueStore(private val settings: Settings) {
    fun getString(
        key: String,
        default: String? = null,
    ): String? = settings.getStringOrNull(key) ?: default

    fun setString(
        key: String,
        value: String?,
    ) {
        if (value == null) settings.remove(key) else settings.putString(key, value)
    }

    fun getBoolean(
        key: String,
        default: Boolean = false,
    ): Boolean = settings.getBoolean(key, default)

    fun setBoolean(
        key: String,
        value: Boolean,
    ) = settings.putBoolean(key, value)
}
