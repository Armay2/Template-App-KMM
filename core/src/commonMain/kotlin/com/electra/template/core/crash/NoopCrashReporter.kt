package com.electra.template.core.crash

class NoopCrashReporter : CrashReporter {
    override fun setUser(userId: String?) = Unit

    override fun log(breadcrumb: String) = Unit

    override fun recordException(throwable: Throwable) = Unit
}
