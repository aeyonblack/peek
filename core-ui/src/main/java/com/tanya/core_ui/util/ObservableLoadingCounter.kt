package com.tanya.core_ui.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger

/**
 * Observes the loading state of an observable by tracking
 * the value on an atomically incremented/decremented counter
 */
class ObservableLoadingCounter {
    /**
     * A counter that is atomically incremented/decremented
     * Mainly used to switch between 0 and 1 denoting false and true respectively
     */
    private val count = AtomicInteger()

    /**
     * keeps track of the value on [count]
     */
    private val loadingState = MutableStateFlow(count.get())

    /**
     * Transforms the value in [loadingState] by setting it to true
     * if it's greater than 0 and false otherwise
     *
     * returns a flow of booleans
     */
    val observable: Flow<Boolean>
        get() =
        loadingState.map { it > 0 }.distinctUntilChanged()

    /**
     * increments the [count] value by 1 and sets the [loadingState] value
     * to the updated [count]
     */
    fun addLoader() {
        loadingState.value = count.incrementAndGet()
    }

    /**
     * decrements the [count] value by 1 and sets the [loadingState] value
     * to the updated [count]
     */
    fun removeLoader() {
        loadingState.value = count.decrementAndGet()
    }
}