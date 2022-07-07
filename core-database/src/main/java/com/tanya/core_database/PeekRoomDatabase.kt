package com.tanya.core_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.tanya.core_model.PeekDatabase
import com.tanya.core_model.entity.TextScanResult
import com.tanya.core_model.typeconverters.DateConverter

@Database(
    entities = [
        TextScanResult::class
    ],
    version = 1
)
@TypeConverters(DateConverter::class)
abstract class PeekRoomDatabase : RoomDatabase(), PeekDatabase