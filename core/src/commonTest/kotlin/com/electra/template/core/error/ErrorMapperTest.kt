package com.electra.template.core.error

import kotlinx.coroutines.CancellationException
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class ErrorMapperTest {
    @Test
    fun mapsCancellationExceptionByRethrowing() {
        assertFailsWith<CancellationException> {
            ErrorMapper.map(CancellationException("cancel"))
        }
    }

    @Test
    fun mapsUnknownThrowableToAppExceptionUnknown() {
        val mapped = ErrorMapper.map(RuntimeException("boom"))
        assertIs<AppException.Unknown>(mapped)
    }

    @Test
    fun passesThroughAppException() {
        val original = AppException.NotFound("x")
        val mapped = ErrorMapper.map(original)
        assertIs<AppException.NotFound>(mapped)
    }
}
