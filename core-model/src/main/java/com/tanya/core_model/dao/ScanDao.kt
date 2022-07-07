package com.tanya.core_model.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.tanya.core_model.entity.TextScanResult
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanDao {

    @Query("SELECT * FROM scans ORDER BY date_created DESC")
    fun getAllScans(): Flow<List<TextScanResult>>

    @Query("SELECT * FROM scans WHERE scan_id = :id")
    fun getScanById(id: Int): Flow<TextScanResult>

    @Insert
    suspend fun insertScan(scan: TextScanResult): Long

    @Delete
    suspend fun deleteScan(scan: TextScanResult)

}