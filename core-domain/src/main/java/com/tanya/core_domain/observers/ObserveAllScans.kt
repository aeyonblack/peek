package com.tanya.core_domain.observers

import com.tanya.core_domain.SubjectUseCase
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_model.repository.TextScansRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import javax.inject.Singleton

@OptIn(ExperimentalCoroutinesApi::class)
@Singleton
class ObserveAllScans @Inject constructor(
    private val scansRepository: TextScansRepository
): SubjectUseCase<ObserveAllScans.Params, List<TextScanResult>>() {

    override fun createObservable(params: Params) = scansRepository.observeAllScans()

    data class Params(val load: Boolean = true)
}