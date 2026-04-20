package com.electra.template.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

abstract class BaseViewModel<State : Any, SideEffect : Any> : ViewModel() {
    protected abstract val initialState: State

    private val _state by lazy { MutableStateFlow(initialState) }
    val state: StateFlow<State> by lazy { _state.asStateFlow() }

    private val _effects = MutableSharedFlow<SideEffect>(extraBufferCapacity = 8)
    val effects: SharedFlow<SideEffect> = _effects.asSharedFlow()

    protected val scope: CoroutineScope get() = viewModelScope

    protected fun update(reducer: (State) -> State) { _state.update(reducer) }
    protected suspend fun emit(effect: SideEffect) { _effects.emit(effect) }
}
