package com.tanya.core_domain

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*

abstract class PlainUseCase<in P, R> {
    suspend operator fun invoke(
        params: P
    ) {
        doWork(params)
    }

    protected abstract suspend fun doWork(params: P): R
}

@OptIn(ExperimentalCoroutinesApi::class)
abstract class SuspendingWorkUseCase<P: Any, R> : SubjectUseCase<P,R>() {
    override fun createObservable(params: P) = flow {
        emit(doWork(params))
    }

    abstract suspend fun doWork(params: P): R
}

@ExperimentalCoroutinesApi
abstract class SubjectUseCase<P: Any,R> {
    private val paramState = MutableSharedFlow<P>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val flow = paramState
        .distinctUntilChanged()
        .flatMapLatest { createObservable(it) }
        .distinctUntilChanged()

    operator fun invoke(params: P) {
        paramState.tryEmit(params)
    }

    protected abstract fun createObservable(params: P): Flow<R>
}