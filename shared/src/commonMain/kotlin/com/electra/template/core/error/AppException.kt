package com.electra.template.core.error

sealed class AppException(message: String? = null, cause: Throwable? = null) : Exception(message, cause) {
    class Network(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class NotFound(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Unauthorized(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Validation(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
    class Unknown(message: String? = null, cause: Throwable? = null) : AppException(message, cause)
}
