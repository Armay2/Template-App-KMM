package com.electra.template.core.error

import kotlinx.coroutines.CancellationException

object ErrorMapper {
    fun map(throwable: Throwable): AppException {
        if (throwable is CancellationException) throw throwable
        return when (throwable) {
            is AppException -> throwable
            else -> AppException.Unknown(throwable.message, throwable)
        }
    }
}
