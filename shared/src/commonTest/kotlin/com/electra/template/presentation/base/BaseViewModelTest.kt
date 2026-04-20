package com.electra.template.presentation.base

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class BaseViewModelTest {
    private val dispatcher = StandardTestDispatcher()

    @BeforeTest fun setMain() { Dispatchers.setMain(dispatcher) }
    @AfterTest fun resetMain() { Dispatchers.resetMain() }

    private class CountVm : BaseViewModel<Int, String>() {
        override val initialState = 0
        fun increment() = update { it + 1 }
        suspend fun ping() = emit("pong")
    }

    @Test
    fun initialStateIsExposed() = runTest(dispatcher) {
        val vm = CountVm()
        assertEquals(0, vm.state.value)
    }

    @Test
    fun updateMutatesStateAndEmitsDownstream() = runTest(dispatcher) {
        val vm = CountVm()
        vm.state.test {
            assertEquals(0, awaitItem())
            vm.increment()
            assertEquals(1, awaitItem())
        }
    }

    @Test
    fun emitSendsSideEffect() = runTest(dispatcher) {
        val vm = CountVm()
        vm.effects.test {
            vm.ping()
            assertEquals("pong", awaitItem())
        }
    }
}
