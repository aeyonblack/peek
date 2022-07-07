package com.tanya.core_domain.usescases

import com.tanya.core_domain.UseCase
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_model.repository.TextScansRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaveScanUseCase @Inject constructor(
    private val scansRepository: TextScansRepository
): UseCase<SaveScanUseCase.Params>() {


    override suspend fun doWork(params: Params) {
        scansRepository.saveScan(params.scan)
    }

    data class Params(val scan: TextScanResult)

}