package com.electra.template.data.network

import com.electra.template.core.error.AppException
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

fun Throwable.toAppException(): AppException = when (this) {
    is AppException -> this
    is ClientRequestException -> when (response.status) {
        HttpStatusCode.NotFound -> AppException.NotFound(message)
        HttpStatusCode.Unauthorized -> AppException.Unauthorized(message)
        HttpStatusCode.UnprocessableEntity, HttpStatusCode.BadRequest ->
            AppException.Validation(message)
        else -> AppException.Network(message, this)
    }
    is ServerResponseException -> AppException.Network(message, this)
    else -> AppException.Unknown(message, this)
}
