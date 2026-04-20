package com.electra.template.core.logging

import co.touchlab.kermit.Logger as Kermit

interface Logger {
    fun d(tag: String, message: String, throwable: Throwable? = null)
    fun i(tag: String, message: String, throwable: Throwable? = null)
    fun w(tag: String, message: String, throwable: Throwable? = null)
    fun e(tag: String, message: String, throwable: Throwable? = null)
}

class KermitLogger : Logger {
    override fun d(tag: String, message: String, throwable: Throwable?) =
        Kermit.withTag(tag).d(throwable) { message }
    override fun i(tag: String, message: String, throwable: Throwable?) =
        Kermit.withTag(tag).i(throwable) { message }
    override fun w(tag: String, message: String, throwable: Throwable?) =
        Kermit.withTag(tag).w(throwable) { message }
    override fun e(tag: String, message: String, throwable: Throwable?) =
        Kermit.withTag(tag).e(throwable) { message }
}
