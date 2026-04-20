package com.electra.template.core.analytics

class NoopAnalyticsTracker : AnalyticsTracker {
    override fun track(event: String, properties: Map<String, Any?>) = Unit
    override fun identify(userId: String?, traits: Map<String, Any?>) = Unit
}
