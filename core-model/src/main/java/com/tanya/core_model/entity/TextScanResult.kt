package com.tanya.core_model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class TextScanResult(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "scan_id") val scanId: Long = 0,
    @ColumnInfo(name = "scan_text") val text: String = "",
    @ColumnInfo(name = "scan_title") val title: String = "",
    @ColumnInfo(name = "date_created") val dateCreated: Long = 0,
    @Ignore val success: Boolean = false
) {
    companion object {
        val Default = TextScanResult()
    }
}