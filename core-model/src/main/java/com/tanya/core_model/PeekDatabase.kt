package com.tanya.core_model

import com.tanya.core_model.dao.ScanDao

interface PeekDatabase {

    fun scanDao(): ScanDao

}