package com.electra.template.presentation.base

sealed interface UiStatus {
    data object Idle : UiStatus

    data object Loading : UiStatus

    data class Error(val message: String, val retryable: Boolean) : UiStatus
}
