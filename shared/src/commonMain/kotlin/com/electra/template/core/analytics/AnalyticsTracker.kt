package com.electra.template.core.analytics

interface AnalyticsTracker {
    fun track(event: String, properties: Map<String, Any?> = emptyMap())
    fun identify(userId: String?, traits: Map<String, Any?> = emptyMap())
}
