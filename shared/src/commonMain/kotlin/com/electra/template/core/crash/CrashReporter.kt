package com.electra.template.core.crash

interface CrashReporter {
    fun setUser(userId: String?)

    fun log(breadcrumb: String)

    fun recordException(throwable: Throwable)
}
