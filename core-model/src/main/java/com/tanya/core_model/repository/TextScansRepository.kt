package com.tanya.core_model.repository

import com.tanya.core_model.dao.ScanDao
import com.tanya.core_model.entity.TextScanResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TextScansRepository @Inject constructor(
    private val scanDao: ScanDao
) {

    fun observeAllScans() = scanDao.getAllScans()

    suspend fun saveScan(scan: TextScanResult) {
        scanDao.insertScan(scan)
    }

    suspend fun deleteScan(scan: TextScanResult) {
        scanDao.deleteScan(scan)
    }

}